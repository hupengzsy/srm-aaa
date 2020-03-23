package org.hzero.iam.app.service.impl;

import com.google.common.collect.Sets;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.algorithm.tree.Node;
import org.hzero.core.algorithm.tree.TreeBuilder;
import org.hzero.core.base.BaseConstants;
import org.hzero.iam.api.dto.RoleDTO;
import org.hzero.iam.app.service.RoleService;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.entity.RolePermission;
import org.hzero.iam.domain.entity.Tenant;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.repository.RolePermissionRepository;
import org.hzero.iam.domain.repository.RoleRepository;
import org.hzero.iam.domain.repository.UserRepository;
import org.hzero.iam.domain.service.MenuCoreService;
import org.hzero.iam.domain.service.role.RoleCreateService;
import org.hzero.iam.domain.service.role.RolePermissionSetAssignService;
import org.hzero.iam.domain.service.role.RolePermissionSetRecycleService;
import org.hzero.iam.domain.service.role.RoleUpdateService;
import org.hzero.iam.domain.service.role.impl.RoleCreateInternalService;
import org.hzero.iam.domain.vo.RoleVO;
import org.hzero.iam.infra.common.utils.UserUtils;
import org.hzero.iam.infra.constant.CheckedFlag;
import org.hzero.iam.infra.constant.HiamMenuType;
import org.hzero.iam.infra.constant.RolePermissionType;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色应用服务默认实现类
 *
 * @author jiangzhou.bo@hand-china.com 2018/06/20 10:39
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleCreateService roleCreateService;
    @Autowired
    private RoleUpdateService roleUpdateService;
    @Autowired
    private RolePermissionSetAssignService rolePermissionSetAssignService;
    @Autowired
    private RolePermissionSetRecycleService rolePermissionSetRecycleService;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private RoleCreateInternalService roleCreateInternalService;
    @Autowired
    private MenuCoreService menuCoreService;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role createRole(Role role, boolean inherited, boolean duplicate) {
        CustomUserDetails details = UserUtils.getUserDetails();
        User adminUser = new User();
        adminUser.setId(details.getUserId());
        return roleCreateService.createRole(role, adminUser, inherited, duplicate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role createRoleByRoleTpl(User user, Tenant tenant, Role parentRole, String roleTplCode) {
        // parentRole 必传参数校验
        Assert.notNull(parentRole, BaseConstants.ErrorCode.DATA_INVALID);
        Assert.notNull(parentRole.getId(), BaseConstants.ErrorCode.DATA_INVALID);
        Assert.notNull(user.getId(), BaseConstants.ErrorCode.DATA_INVALID);

        // tenant 必传参数校验 以及 模板参数校验
        Assert.notNull(tenant, BaseConstants.ErrorCode.DATA_INVALID);
        Assert.notNull(tenant.getTenantId(), BaseConstants.ErrorCode.DATA_INVALID);
        Assert.isTrue(StringUtils.isNotEmpty(roleTplCode), BaseConstants.ErrorCode.DATA_INVALID);

        // 入参日志打印
        LOGGER.info("parent role is: {}\n tenant is {}\n roleTplCode is {}",
                parentRole.toString(), tenant.toString(), roleTplCode);

        // 获取租户管理员模板信息
        Role params = new Role();
        params.setCode(roleTplCode);
        Role roleTpl = roleRepository.selectOne(params);
        Assert.notNull(roleTpl, BaseConstants.ErrorCode.DATA_INVALID);

        // 复制模板角色的信息
        Role role = new Role();
        role.setTenantId(tenant.getTenantId());
        role.setInheritRoleId(roleTpl.getId());
        // 初始化管理员角色名称
        role.initRoleNameByRoleTpl(user.getLanguage(), roleTpl);
        // 获取角色编码简码: 取模板中以/为分隔符的最后一个字段。eg: /tpl/default ---> default
        String[] tplCodeSplit = StringUtils.split(roleTpl.getCode(), BaseConstants.Symbol.SLASH);
        String simpleCode = tplCodeSplit[tplCodeSplit.length - 1];
        role.setCode(simpleCode);
        // 设置父级角色id
        role.setParentRoleId(parentRole.getId());

        try {
            return roleCreateInternalService.createRole(role, user, true, false);
        } catch (CommonException e) {
            if ("hiam.warn.role.codeExist".equalsIgnoreCase(e.getCode())) {
                LOGGER.warn("createRoleByRoleTpl: role code exists, role={}", role);


                params.setTenantId(tenant.getTenantId());
                params.setCode(role.getCode());
                params.setParentRoleId(role.getParentRoleId());
                params.setParentRoleAssignLevel(role.getParentRoleAssignLevel());
                params.setParentRoleAssignLevelValue(role.getParentRoleAssignLevelValue());
                params.setCreatedByTenantId(user.getOrganizationId());
//                params.setCode(role.getCode());
//                params.setTenantId(tenant.getTenantId());
                return roleRepository.selectOne(params);
            }
            throw e;
        }
    }

    @Override
    public List<Role> listTenantAdmin(Long tenantId) {
        return roleRepository.listTenantAdmin(tenantId);
    }

    @Override
    public List<RoleDTO> listTreeList(RoleVO roleVO) {

        CustomUserDetails self = UserUtils.getUserDetails();
        roleVO = Optional.ofNullable(roleVO).orElse(new RoleVO());
        roleVO.setUserId(self.getUserId());

        /**
         * 查询组装树所需原料数据
         */
        List<RoleVO> roleVOList = roleRepository.selectUserManageableRoleTree(roleVO);
        List<RoleDTO> treeNodes = RoleDTO.convertEntityToTreeDTO(roleVOList);
        treeNodes.forEach(item->{
            if(treeNodes.stream().noneMatch(parent->parent.getId().equals(item.getParentRoleId()))){
                item.setParentRoleId(null);
            }
        });

        /**
         * 构建树
         */
        return TreeBuilder.buildTree(treeNodes, -1L, new Node<Long, RoleDTO>() {
            @Override
            public Long getKey(RoleDTO obj) {
                return obj.getId();
            }

            @Override
            public Long getParentKey(RoleDTO obj) {
                // 检查父级节点是否存在
                return obj.getParentRoleId() == null ? -1L : obj.getParentRoleId();
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role updateRole(Role role) {
        return roleUpdateService.updateRole(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableRole(Long roleId) {
        changeRoleEnableFlag(roleId, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableRole(Long roleId) {
        changeRoleEnableFlag(roleId, false);
    }

    public void changeRoleEnableFlag(Long roleId, boolean enableFlag) {
        Assert.notNull(roleId, "role id cannot be null.");
        Role self = this.roleRepository.selectByPrimaryKey(roleId);
        if (self == null) {
            throw new CommonException("hiam.error.role.updateRoleNotFound");
        }

        // 如果是启用角色, 则其父级角色必须全部是启用状态
        if (enableFlag) {
            List<Role> parentRoleList = roleRepository.selectParentRoles(roleId);
            List<Role> disabledParentRoleList =
                    parentRoleList.parallelStream().filter(p -> !p.getEnabled()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(disabledParentRoleList)) {
                throw new CommonException("error.role.enable.parentRole.disabled");
            }
        }

        // 启用或禁用当前角色
        self.setEnabled(enableFlag);
        roleRepository.updateOptional(self, Role.FIELD_IS_ENABLED);

        // 同步启用或禁用所有子孙角色
        List<Role> subRoleList = roleRepository.selectAllSubRoles(roleId);
        if (CollectionUtils.isNotEmpty(subRoleList)) {
            subRoleList.forEach(r -> r.setEnabled(enableFlag));
            roleRepository.batchUpdateOptional(subRoleList, Role.FIELD_IS_ENABLED);
        }

        LOGGER.info("enable or disable role and subRole. enableFlag={}, roleId={}", enableFlag, roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignRolePermissionSets(Long roleId, Long[] permissionSetIds, String type) {
        rolePermissionSetAssignService.assignRolePermissionSets(roleId, Sets.newHashSet(permissionSetIds), type);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recycleRolePermissionSets(Long roleId, Long[] permissionSetIds, String type) {
        rolePermissionSetRecycleService.recycleRolePermissionSets(roleId, Sets.newHashSet(permissionSetIds), type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void directAssignRolePermission(RolePermission rolePermission) {
        String type = RolePermissionType.value(rolePermission.getType()).name();

        Assert.isTrue(CollectionUtils.isNotEmpty(rolePermission.getPermissionSetIds()), "permissionSetIds is empty.");

        Long roleId = rolePermission.getRoleId();

        // 角色ID、角色编码只能传一个
        if (roleId != null) {
            if (roleRepository.selectRoleSimple(roleId) == null) {
                throw new CommonException("hiam.warn.roleNotFoundForId", roleId);
            }
        } else if (StringUtils.isNotBlank(rolePermission.getRoleCode())) {
            Role role = roleRepository.selectRoleSimple(rolePermission.getRoleCode());
            if (role == null) {
                throw new CommonException("hiam.warn.roleNotFoundForCode", rolePermission.getRoleCode());
            }
            roleId = role.getId();
        }

        // 默认设置创建标识
        String createFlag = StringUtils.defaultIfBlank(rolePermission.getCreateFlag(), CheckedFlag.CHECKED.value());
        String inheritFlag = StringUtils.defaultIfBlank(rolePermission.getInheritFlag(), CheckedFlag.UNCHECKED.value());

        Long finalRoleId = roleId;
        List<RolePermission> rolePermissionList = rolePermission.getPermissionSetIds().stream()
                .map(permissionId -> new RolePermission(finalRoleId, permissionId, inheritFlag, createFlag, type))
                .collect(Collectors.toList());

        RolePermission params = new RolePermission();
        params.setRoleId(roleId);
        params.setType(type);
        for (RolePermission permission : rolePermissionList) {
            params.setPermissionSetId(permission.getPermissionSetId());
            RolePermission exists = rolePermissionRepository.selectOne(params);
            if (exists != null) {
                rolePermissionRepository.updateByPrimaryKey(exists);
            } else {
                rolePermissionRepository.insertSelective(permission);
            }
        }
    }

    // FIX:不需要事务，方法幂等。该方法执行的更新sql数量较大，TiDB一个事务内默认更新数量5000，因此去掉.
    //@Transactional(rollbackFor = Exception.class)
    @Override
    public int initRoleLevelPath(boolean initAll) {
        Set<Long> ids = new HashSet<>(64);

        // 查询预置角色
        List<Role> builtInRoles = roleRepository.selectBuiltInRoles(true);

        // 初始化超级管理员
        List<Role> superRoles = builtInRoles.stream().filter(r -> Role.ROOT_ID.equals(r.getParentRoleId())).collect(Collectors.toList());
        for (Role role : superRoles) {
            if (initAll || StringUtils.isAnyBlank(role.getLevelPath(), role.getInheritLevelPath())) {
                role.initSupperRoleLevelPath();
                roleRepository.updateOptional(role, Role.FIELD_LEVEL_PATH, Role.FIELD_INHERIT_LEVEL_PATH);
                ids.add(role.getId());
            }
        }

        List<Role> inheritRootRoles = roleRepository.selectByInheritRoleId(Role.ROOT_ID);
        for (Role role : inheritRootRoles) {
            if (initAll || StringUtils.isBlank(role.getInheritLevelPath())) {
                if (role.getParentRoleId() == null || role.getInheritRoleId() == null) {
                    LOGGER.warn("role's parentRoleId or inheritRoleId is null, role={}", role);
                    continue;
                }
                role.buildInheritRoleLevelPath(null, null);
                roleRepository.updateOptional(role, Role.FIELD_INHERIT_LEVEL_PATH);
                ids.add(role.getId());
            }
        }

        // 最后在统一更新，因为采用了多线程，可避免出现版本号不一致的情况
        Map<Long, Role> idMap = new HashMap<>(64);

        superRoles.forEach(role -> {
            // 递归更新子角色路径
            initCreatedSubRolesLevelPath(role, idMap, initAll);
        });

        idMap.values().parallelStream().forEach(role -> {
            roleRepository.updateOptional(role, Role.FIELD_LEVEL_PATH, Role.FIELD_INHERIT_LEVEL_PATH);
        });

        ids.addAll(idMap.keySet());
        LOGGER.info("init role path success, roleIds={}", ids);

        return ids.size();
    }

    @Override
    public void initRoleOfInheritSubRoleTreePss(String[] roleCodes) {
        List<Role> roles;
        if (ArrayUtils.isNotEmpty(roleCodes)) {
            // 根据编码查询
            roles = roleRepository.selectByCondition(
                    Condition.builder(Role.class).andWhere(
                            Sqls.custom().andIn(Role.FIELD_CODE, Arrays.asList(roleCodes))).build());
        } else {
            // 查询预置角色 不包含超级管理员
            roles = roleRepository.selectBuiltInRoles(false);
        }

        rolePermissionSetAssignService.assignInheritedRolePermissionSets(roles);
    }


    @Override
    public Map<String, Object> fixData() {
        Map<String, Object> result = new HashMap<>(8);


        List<Role> roles = roleRepository.selectAll();
        Map<Long, User> userMap = new HashMap<>(128);
        int updateUserCount = 0;
        int userNotFoundCount = 0;
        int updateRoleCount = 0;
        for (Role role : roles) {
            // 处理 createByTenantId
            User user = userMap.get(role.getCreatedBy());
            if (user == null) {
                user = userRepository.selectUserTenant(role.getCreatedBy());
                userMap.put(role.getCreatedBy(), user);
            }
            if (user == null) {
                LOGGER.warn("role createdBy user not found. createdBy = {}", role.getCreatedBy());
                userNotFoundCount++;
            }
            else {
                role.setCreatedByTenantId(user.getOrganizationId());
                updateUserCount ++;
            }

            // 角色编码调整
            String newCode = "";
            if (role.getBuiltIn() != null && role.getBuiltIn()) {
                // 内置角色保持原编码
                newCode = role.getCode();
            } else {
                // 非内置角色取最后一段
                String[] arr = role.getCode().split(BaseConstants.Symbol.SLASH);
                newCode = arr[arr.length - 1];
            }
            role.setCode(newCode);

            // 更新
            roleRepository.updateOptional(role, Role.FIELD_CREATED_BY_TENANT_ID, Role.FIELD_CODE);
        }

        // levelPath
        int updateRoleLevelPathCount = initRoleLevelPath(true);

        int updateMenuLevelPathCount = 0;
        for (HiamMenuType menuType : HiamMenuType.values()) {
            updateMenuLevelPathCount += menuCoreService.initLevelPath(menuType, true);
        }

        result.put("updateRole[createdByTenantId]Count", updateUserCount);
        result.put("role[createdBy]NotFoundCount", userNotFoundCount);
        result.put("updateRole[code]Count", updateRoleCount);
        result.put("updateRole[levelPath]Count", updateRoleLevelPathCount);
        result.put("updateMenu[levelPath]Count", updateMenuLevelPathCount);

        return result;
    }

    private void initCreatedSubRolesLevelPath(Role parentRole, Map<Long, Role> idMap, boolean initAll) {
        List<Role> subRoles = roleRepository.selectByParentRoleId(parentRole.getId());
        subRoles.parallelStream().forEach(role -> {
            if (initAll || StringUtils.isBlank(role.getLevelPath())) {
                role.buildCreatedRoleLevelPath(parentRole);
                if (idMap.containsKey(role.getId())) {
                    idMap.get(role.getId()).setLevelPath(role.getLevelPath());
                } else {
                    idMap.put(role.getId(), role);
                }
            }
            // 初始化继承的角色
            initInheritedSubRolesLevelPath(role, idMap, initAll);
            // 递归子角色
            initCreatedSubRolesLevelPath(role, idMap, initAll);
        });
    }

    private void initInheritedSubRolesLevelPath(Role inheritRole, Map<Long, Role> idMap, boolean initAll) {
        List<Role> subRoles = roleRepository.selectByInheritRoleId(inheritRole.getId());
        for (Role role : subRoles) {
            if (initAll || StringUtils.isBlank(role.getInheritLevelPath())) {
                Role parentRole = idMap.get(role.getParentRoleId());
                if (parentRole == null) {
                    parentRole = roleRepository.selectById(role.getId());
                }
                role.buildInheritRoleLevelPath(parentRole, inheritRole);
                if (idMap.containsKey(role.getId())) {
                    idMap.get(role.getId()).setInheritLevelPath(role.getInheritLevelPath());
                } else {
                    idMap.put(role.getId(), role);
                }
            }
        }
    }

}
