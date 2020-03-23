package org.hzero.iam.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants.Flag;
import org.hzero.core.cache.ProcessCacheValue;
import org.hzero.iam.api.dto.PermissionSetSearchDTO;
import org.hzero.iam.api.dto.RoleDTO;
import org.hzero.iam.api.dto.RolePermissionCheckDTO;
import org.hzero.iam.domain.entity.Menu;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.entity.RolePermission;
import org.hzero.iam.domain.repository.MenuRepository;
import org.hzero.iam.domain.repository.RoleRepository;
import org.hzero.iam.domain.vo.RoleVO;
import org.hzero.iam.infra.common.utils.HiamMenuUtils;
import org.hzero.iam.infra.common.utils.HiamRoleUtils;
import org.hzero.iam.infra.common.utils.UserUtils;
import org.hzero.iam.infra.constant.HiamMemberType;
import org.hzero.iam.infra.mapper.RolePermissionMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.srm.iam.infra.mapper.SrmRoleMapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author ASUS 2020/02/03 22:27
 */

@Component
public class RoleRepositoryImpl extends BaseRepositoryImpl<Role> implements RoleRepository {
    public static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryImpl.class);
    private static final RoleVO EMPTY_ROLE = new RoleVO();
    @Autowired
    private SrmRoleMapper srmRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private MenuRepository menuRepository;

    public RoleRepositoryImpl() {
    }

    @Override
    @ProcessLovValue
    @ProcessCacheValue
    public Page<RoleVO> selectSelfManageableRoles(RoleVO params, PageRequest pageRequest) {
        CustomUserDetails self = UserUtils.getUserDetails();
        params = Optional.ofNullable(params).orElse(new RoleVO());
        params.setUserId(self.getUserId());
        RoleVO finalParams = changeParams(params);
        return PageHelper.doPage(pageRequest, () -> this.srmRoleMapper.selectUserManageableRoles(finalParams));
    }

    /**
     * 处理参数：
     * 1. 添加 从管理角色的路径查询子孙角色的查询条件
     * 2. 字符串参数转义
     *
     * @param params
     * @return
     */
    private RoleVO changeParams(RoleVO params) {
        RoleVO copyParams = new RoleVO();
        BeanUtils.copyProperties(params, copyParams);
        if(params.enableIsNull()){
            //isEnable参数特殊处理
            copyParams.setEnabled(null);
        }
        //添加 从管理角色的路径查询子孙角色的查询条件
        List<String> rolePath = srmRoleMapper.selectParentManageRoles(copyParams);
        StringBuilder paramSb = new StringBuilder();
        if(rolePath.isEmpty()){
            copyParams.setParentManageParamStr(" 1 = 2 ");
        }else{
            rolePath.forEach(onePath -> paramSb.append(" ir.h_level_path LIKE '").append(StringEscapeUtils.escapeSql(onePath)).append("%' OR"));
            copyParams.setParentManageParamStr("(" + paramSb.substring(0, paramSb.length() - 2) + ")");
        }


        //字符串参数转义
        if (!StringUtils.isEmpty(copyParams.getName())) {
            copyParams.setName(StringEscapeUtils.escapeSql(copyParams.getName()));
        }
        if (!StringUtils.isEmpty(copyParams.getTenantName())) {
            copyParams.setTenantName(StringEscapeUtils.escapeSql(copyParams.getTenantName()));
        }
        if(!StringUtils.isEmpty(copyParams.getCode())){
            copyParams.setCode(StringEscapeUtils.escapeSql(copyParams.getCode()));
        }
        if(!StringUtils.isEmpty(copyParams.getLevel())){
            copyParams.setLevel(StringEscapeUtils.escapeSql(copyParams.getLevel()));
        }
        if(!StringUtils.isEmpty(copyParams.getParentRoleAssignLevel())){
            copyParams.setParentRoleAssignLevel(StringEscapeUtils.escapeSql(copyParams.getParentRoleAssignLevel()));
        }
        return copyParams;
    }

    @Override
    public List<RoleVO> selectSelfAllManageableRoles(RoleVO params) {
        CustomUserDetails self = UserUtils.getUserDetails();
        params = Optional.ofNullable(params).orElse(new RoleVO());
        params.setUserId(self.getUserId());
        params.setSelectAssignedRoleFlag(true);
        RoleVO finaParam=changeParams(params);
        return this.srmRoleMapper.selectUserManageableRoles(finaParam);
    }

    @Override
    public Page<RoleVO> selectSelfAssignableRoles(RoleVO params, PageRequest pageRequest) {
        CustomUserDetails self = UserUtils.getUserDetails();
        params = Optional.ofNullable(params).orElse(new RoleVO());
        params.setUserId(self.getUserId());
        params.setEnabled(true);
        if (CollectionUtils.isNotEmpty(params.getExcludeUserIds())) {
            Long excludeUserId = (Long) params.getExcludeUserIds().get(0);
            params.setExcludeUserId(excludeUserId);
        }
        params.setSelectAssignedRoleFlag(true);
        RoleVO finalParams = changeParams(params);
        return PageHelper.doPage(pageRequest, () -> this.srmRoleMapper.selectUserManageableRoles(finalParams));
    }

    @Override
    @ProcessLovValue
    public RoleVO selectRoleDetails(Long roleId) {
        return this.srmRoleMapper.selectRoleDetails(roleId);
    }

    @Override
    public List<RoleVO> selectSelfAdminRoles(RoleVO params, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> {
            return this.selectSelfAdminRoles(params);
        });
    }

    @Override
    public List<RoleVO> selectSelfAdminRoles(RoleVO params) {
        CustomUserDetails self = UserUtils.getUserDetails();
        params = (RoleVO) Optional.ofNullable(params).orElse(new RoleVO());
        params.setUserId(self.getUserId());
        return this.srmRoleMapper.selectUserAdminRoles(params);
    }

    @Override
    public List<RoleVO> selectSelfCurrentTenantRoles() {
        CustomUserDetails self = UserUtils.getUserDetails();
        RoleVO params = new RoleVO();
        params.setMemberId(self.getUserId());
        params.setMemberType(HiamMemberType.USER.value());
        params.setTenantId(self.getTenantId());
        List<RoleVO> selfRoles = this.selectMemberRoles(params);
        List<RoleVO> returnRoles = new ArrayList(selfRoles.size());
        if (self.isRoleMergeFlag()) {
            Map<String, List<RoleVO>> map = (Map) selfRoles.stream().collect(Collectors.groupingBy(RoleVO::getLevel));
            if (map.size() > 1) {
                map.forEach((level, roles) -> {
                    RoleVO role = (RoleVO) roles.get(0);
                    role.setName(RoleVO.obtainRoleName(role.getLevel(), role.getName(), self.getLanguage()));
                    returnRoles.add(role);
                });
            }
        } else {
            returnRoles.addAll(selfRoles);
        }

        return returnRoles;
    }

    @Override
    public RoleVO selectCurrentRole() {
        CustomUserDetails details = UserUtils.getUserDetails();
        if (details.getRoleId() == null) {
            return EMPTY_ROLE;
        } else {
            RoleVO params = new RoleVO();
            params.setMemberId(details.getUserId());
            params.setMemberType(HiamMemberType.USER.value());
            params.setTenantId(details.getTenantId());
            params.setId(details.getRoleId());
            List<RoleVO> roles = this.selectMemberRoles(params);
            return CollectionUtils.isNotEmpty(roles) ? (RoleVO) roles.get(0) : EMPTY_ROLE;
        }
    }

    @Override
    @ProcessLovValue
    public Page<RoleVO> selectSimpleRolesWithTenant(RoleVO params, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> {
            return this.srmRoleMapper.selectSimpleRoles(params);
        });
    }

    @Override
    public RoleVO selectSimpleRoleWithTenant(RoleVO params) {
        List<RoleVO> list = this.srmRoleMapper.selectSimpleRoles(params);
        return CollectionUtils.isNotEmpty(list) && list.get(0) != null ? (RoleVO) list.get(0) : null;
    }

    @Override
    public List<Menu> selectRolePermissionSetTree(Long roleId, PermissionSetSearchDTO permissionSetParam) {
        Role role = (Role) this.selectByPrimaryKey(roleId);
        Assert.notNull(role, "role is invalid");
        permissionSetParam.setAllocateRoleId(roleId);
        Long currentRoleId = role.getParentRoleId();
        List<Menu> menuList = this.menuRepository.selectRolePermissionSet(currentRoleId, roleId, permissionSetParam);
        return HiamMenuUtils.formatMenuListToTree(menuList, Boolean.TRUE);
    }

    @Override
    public int countRoleByCode(String roleCode) {
        return this.selectCountByCondition(Condition.builder(Role.class).andWhere(Sqls.custom().andEqualTo("code", roleCode)).build());
    }

    @Override
    public List<Role> selectParentRoles(Long roleId) {
        return this.srmRoleMapper.selectAllParentRoles(roleId);
    }

    @Override
    public List<Role> selectAllSubRoles(Long roleId) {
        return this.srmRoleMapper.selectAllSubRoles(roleId);
    }

    @Override
    public Role selectRoleSimple(Long roleId) {
        return (Role) this.selectOneOptional((new Role()).setId(roleId), (new Criteria()).select(new String[]{"id", "code", "name", "level", "isEnabled", "tenantId", "parentRoleAssignLevel", "parentRoleAssignLevelValue"}).where(new String[]{"id"}));
    }

    @Override
    public Role selectRoleSimple(String roleCode) {
        return (Role) this.selectOneOptional((new Role()).setCode(roleCode), (new Criteria()).select(new String[]{"id", "code", "tenantId", "level"}).where(new String[]{"code"}));
    }

    @Override
    public List<RoleVO> selectMemberRoles(RoleVO params) {
        return this.srmRoleMapper.selectMemberRoles(params);
    }

    @Override
    public List<Role> selectBuiltInRoles(boolean includeSuperAdmin) {
        return includeSuperAdmin ? this.selectByCondition(Condition.builder(Role.class).andWhere(Sqls.custom().andEqualTo("isBuiltIn", Flag.YES)).build()) : this.selectByCondition(Condition.builder(Role.class).andWhere(Sqls.custom().andEqualTo("isBuiltIn", Flag.YES).andNotEqualTo("parentRoleId", Role.ROOT_ID)).build());
    }

    @Override
    @ProcessLovValue
    public Page<RoleVO> selectMemberRoles(Long memberId, HiamMemberType memberType, PageRequest pageRequest) {
        RoleVO params = new RoleVO();
        params.setMemberId(memberId);
        params.setMemberType(memberType.value());
        Page<RoleVO> page = PageHelper.doPage(pageRequest, () -> {
            return this.srmRoleMapper.selectMemberRoles(params);
        });
        List<RoleVO> selfAllRoles = this.selectSelfAllManageableRoles((RoleVO) null);
        Set<Long> allRoleIds = (Set) selfAllRoles.stream().map(RoleVO::getId).collect(Collectors.toSet());
        Iterator var8 = page.getContent().iterator();

        while (var8.hasNext()) {
            RoleVO role = (RoleVO) var8.next();
            if (allRoleIds.contains(role.getId())) {
                role.setManageableFlag(Flag.YES);
            }
        }

        return page;
    }

    @Override
    public List<Role> selectInheritSubRoleTreeWithPermissionSets(Long inheritRoleId, Set<Long> permissionSetIds, String type) {
        List<Role> inheritSubRoles = this.srmRoleMapper.selectInheritSubRolesWithPermissionSets(inheritRoleId, permissionSetIds, type);
        return HiamRoleUtils.formatRoleListToTree(inheritSubRoles, true);
    }

    @Override
    public List<Role> selectCreatedSubRoleTreeWithPermissionSets(Long parentRoleId, Set<Long> permissionSetIds, String type) {
        List<Role> createdSubRoles = this.srmRoleMapper.selectCreatedSubRolesWithPermissionSets(parentRoleId, permissionSetIds, type);
        return HiamRoleUtils.formatRoleListToTree(createdSubRoles, false);
    }

    @Override
    public int countUserRole(Long userId, Long roleId) {
        return this.srmRoleMapper.countUserRole(userId, roleId);
    }

    @Override
    public Role selectById(Long id) {
        Role param = new Role();
        param.setId(id);
        List<Role> roles = this.srmRoleMapper.selectRoleAssignInfo(param);
        return CollectionUtils.isNotEmpty(roles) ? (Role) roles.get(0) : null;
    }

    @Override
    public List<Role> selectByInheritRoleId(Long id) {
        Role param = new Role();
        param.setInheritRoleId(id);
        return this.srmRoleMapper.selectRoleAssignInfo(param);
    }

    @Override
    public List<Role> selectByParentRoleId(Long id) {
        Role param = new Role();
        param.setParentRoleId(id);
        return this.srmRoleMapper.selectRoleAssignInfo(param);
    }

    @Override
    public RoleVO selectAdminRole(Long roleId) {
        RoleVO params = new RoleVO();
        params.setId(roleId);
        params.setUserId(UserUtils.getUserDetails().getUserId());
        return this.srmRoleMapper.selectAdminRole(params);
    }

    @Override
    public List<RolePermission> selectRolePermissions(RolePermission params) {
        return this.rolePermissionMapper.selectRolePermissionSets(params);
    }

    @Override
    public List<Role> listTenantAdmin(Long tenantId) {
        return this.srmRoleMapper.listTenantAdmin(tenantId);
    }

    @Override
    @ProcessLovValue
    @ProcessCacheValue
    public Page<RoleDTO> selectUserManageableRoleTree(PageRequest pageRequest, RoleVO params) {
        CustomUserDetails self = UserUtils.getUserDetails();
        params = (RoleVO) Optional.ofNullable(params).orElse(new RoleVO());
        params.setUserId(self.getUserId());
        if (Objects.equals(params.getParentRoleId(), -1L)) {
            params.setQueryRootNodeFlag(1);
            params.setParentRoleId((Long) null);
        }

        RoleVO finalParams = params;
        Page<RoleVO> page = PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmRoleMapper.selectUserManageableRoleTree(finalParams);
        });
        List<RoleDTO> roleDTOList = (List) page.getContent().stream().map((item) -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(item, roleDTO);
            if (roleDTO.getChildrenNum() != null && roleDTO.getChildrenNum() > 0) {
                roleDTO.addChildren(Collections.emptyList());
            }

            return roleDTO;
        }).collect(Collectors.toList());
        Page<RoleDTO> roleDTOPage = new Page();
        BeanUtils.copyProperties(page, roleDTOPage);
        roleDTOPage.setContent(roleDTOList);
        return roleDTOPage;
    }

    @Override
    public List<Role> listRole(Long tenantId, Long userId) {
        return this.srmRoleMapper.listRole(tenantId, userId);
    }

    @Override
    @ProcessLovValue
    @ProcessCacheValue
    public List<RoleVO> selectUserManageableRoleTree(RoleVO params) {
        return this.srmRoleMapper.selectUserManageableRoleTree(params);
    }

    @Override
    public boolean checkPermission(RolePermissionCheckDTO rolePermissionCheckDTO) {
        Assert.notNull(rolePermissionCheckDTO, "parameter cannot be null.");
        rolePermissionCheckDTO.validate();
        return this.rolePermissionMapper.checkPermission(rolePermissionCheckDTO) > 0;
    }
}
