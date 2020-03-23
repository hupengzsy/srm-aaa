package org.srm.iam.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.servicecomb.pack.omega.transaction.annotations.Compensable;
import org.hzero.core.base.AopProxy;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.iam.app.service.UserService;
import org.hzero.iam.domain.entity.MemberRole;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.entity.UserConfig;
import org.hzero.iam.domain.entity.UserInfo;
import org.hzero.iam.domain.repository.MemberRoleRepository;
import org.hzero.iam.domain.repository.RoleRepository;
import org.hzero.iam.domain.repository.UserConfigRepository;
import org.hzero.iam.domain.repository.UserRepository;
import org.hzero.iam.domain.vo.RoleVO;
import org.hzero.iam.infra.mapper.RoleMapper;
import org.hzero.iam.infra.mapper.UserInfoMapper;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.srm.boot.saga.infra.annotation.SagaCompensate;
import org.srm.boot.saga.utils.SagaClient;
import org.srm.iam.api.dto.TenantEsDTO;
import org.srm.iam.api.dto.UserDTO;
import org.srm.iam.app.service.CompanyImportService;
import org.srm.iam.domain.entity.UserEs;
import org.srm.iam.domain.repository.CompanyImportRepository;
import org.srm.iam.domain.repository.UserEsRepository;
import org.srm.iam.infra.constant.IamConstants;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.srm.iam.infra.constant.IamConstants.ErrorCode.PLEASE_IMPORT_TENANT_FIRST;

/**
 * 供应商导入serviceImpl
 *
 * @author peng.yang03@hand-china.com 2019/09/10 10:18
 */
@Service
public class CompanyImportServiceImpl implements CompanyImportService, AopProxy<CompanyImportService> {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository hiamUserRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    private MemberRoleRepository memberRoleRepository;
    @Autowired
    private UserConfigRepository userConfigRepository;
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private CompanyImportRepository companyImportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserEsRepository userEsRepository;
    @Autowired
    private RoleMapper roleMapper;


    private static final String METHOD_NAME = "IAM-COMPANTYIMPORTSERVICEIMPL-REGISTERUSER";

    private final Logger logger = LoggerFactory.getLogger(CompanyImportServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Compensable(compensationMethod = "registerUserRollBack")
    public User registerUser(String sagaKey, User user) {

        // 幂等处理
        if (SagaClient.checkSagaKeyExist(sagaKey, METHOD_NAME)) {
            List<User> existUsers = hiamUserRepository.selectByCondition(Condition.builder(User.class).andWhere(Sqls.custom().
                    andEqualTo(User.FIELD_EMAIL, user.getEmail()).andEqualTo(User.FIELD_PHONE, user.getPhone())
            ).build());
            Assert.isTrue(CollectionUtils.isNotEmpty(existUsers), BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            return existUsers.get(0);
        } else {
            SagaClient.putInitSagaData(sagaKey, METHOD_NAME);
        }

        // 幂等数据处理
        Map<String, String> map = new HashMap<>(2);
        try {
            String writeValueAsString = objectMapper.writeValueAsString(user);
            map.put("CREATE-TENANTANDUSER-1", writeValueAsString);
            // saga数据缓存
            SagaClient.putSagaData(sagaKey, METHOD_NAME, map);
        } catch (Exception e) {
            logger.error(null, e);
        }

        user.setPhoneCheckFlag(BaseConstants.Flag.YES);
        user.setEmailCheckFlag(BaseConstants.Flag.YES);
        return userService.register(user);
    }

    @Override
    public Map<String, List<UserDTO>> batchCreateUserAccount(List<UserDTO> users) {

        // 数据准备和校验（查询已有租户和用户 校验传入数据是否规范（是否已同步租户）和操作方式（插入或更新））
        List<TenantEsDTO> tenantEsTemp = new ArrayList<>();
        List<UserEs> userEsTemp = new ArrayList<>();
        users.forEach(u -> {
            tenantEsTemp.add(new TenantEsDTO(u.getEsCode(), String.valueOf(u.getOrganizationId())));
            userEsTemp.add(new UserEs(u.getEsCode(), u.getEsUserId()));
        });
        // 查询系统数据
        List<TenantEsDTO> existTenant = companyImportRepository.queryTenantId(tenantEsTemp);
        List<UserEs> existUserEsList = companyImportRepository.queryExistUserEs(userEsTemp);
        Map<String, TenantEsDTO> tenantMap = existTenant.stream().collect(Collectors.toMap(t -> t.getExternalSystemCode().concat(t.getEsTenantId()), Function.identity()));
        Map<String, UserEs> exitUserEsMap = existUserEsList.stream().collect(Collectors.toMap(u -> u.getExternalSystemCode().concat(u.getEsUserId()), Function.identity()));
        List<Long> userIds = existUserEsList.stream().map(UserEs::getUserId).collect(Collectors.toList());
        List<User> userList = userRepository.selectByIds(userIds.stream().map(Objects::toString).collect(Collectors.joining(",")));
        Map<Long, List<User>> userMap = userList.stream().collect(Collectors.groupingBy(User::getId));
        // 分类 （是否已同步过租户）
        List<UserDTO> exitTenantUser = users.stream().filter(u -> tenantMap.get(u.getEsCode().concat(String.valueOf(u.getOrganizationId()))) != null).collect(Collectors.toList());
        List<UserDTO> noTenantUser = users.stream().filter(u -> !exitTenantUser.contains(u)).collect(Collectors.toList());

        List<UserDTO> successUser = new ArrayList<>();
        List<UserDTO> failureUser = new ArrayList<>();
        List<Long> tenantIds = existTenant.stream().map(TenantEsDTO::getTenantId).collect(Collectors.toList());
        List<User> adminList = companyImportRepository.queryManagerByRole(tenantIds);
        Map<Long, List<User>> adminUserMap = adminList.stream().collect(Collectors.groupingBy(User::getOrganizationId));
        noTenantUser.forEach(u -> u.setRegisteredMessage(PLEASE_IMPORT_TENANT_FIRST));

        if (CollectionUtils.isNotEmpty(exitTenantUser)) {
            exitTenantUser.forEach(u -> {
                // 数据准备
                UserEs userEs;
                int importFlag;
                if (exitUserEsMap.get(u.getEsCode().concat(u.getEsUserId())) == null) {
                    userEs = new UserEs(u.getEsCode(), null, u.getEsUserId(), u.getDataVersion());
                    importFlag = BaseConstants.Flag.YES;
                } else {
                    this.initUpdateUser(u, exitUserEsMap, userMap);
                    importFlag = BaseConstants.Flag.NO;
                    userEs = exitUserEsMap.get(u.getEsCode().concat(u.getEsUserId()));
                    userEs.setDataVersion(u.getDataVersion());
                }
                u.setOrganizationId(tenantMap.get(u.getEsCode().concat(String.valueOf(u.getOrganizationId()))).getTenantId());

                // 数据处理
                try {
                    // 处理角色
                    if (CollectionUtils.isNotEmpty(u.getMemberRoleList())) {
                        this.initMemberRole(u, adminUserMap);
                    }
                    self().createUserAccount(u, userEs, importFlag);
                    successUser.add(u);
                } catch (Exception e) {
                    u.setRegisteredMessage(e.getMessage());
                    failureUser.add(u);
                    logger.error("import user-account {} failure, error: {}", u, e);
                }
            });
        }
        // 处理操作结果
        Map<String, List<UserDTO>> result = new HashMap<>(2);
        result.put(IamConstants.SUCCESS_STATUS, successUser);
        noTenantUser.addAll(failureUser);
        result.put(IamConstants.FAILURE_STATUS, noTenantUser);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserAccount(UserDTO userDTO, UserEs userEs, Integer importFlag) {
        if (BaseConstants.Flag.YES.equals(importFlag)) {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            userService.createUser(user);
            userEs.setUserId(user.getId());
            userEsRepository.insert(userEs);
        } else {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            userService.updateUser(user);
            userEsRepository.updateByPrimaryKey(userEs);
        }
    }

    /**
     * saga补偿方法
     *
     * @param sagaKey sagaKey
     * @throws IOException io异常
     */
    @SagaCompensate(methodKey = METHOD_NAME)
    public void registerUserRollBack(String sagaKey, User dto) throws IOException {
        Map<String, String> readSage = SagaClient.getSagaData(sagaKey, METHOD_NAME);
        String date = readSage.get("CREATE-TENANTANDUSER-1");
        User user = objectMapper.readValue(date, User.class);
        // 获取用户
        List<User> users = hiamUserRepository.selectByCondition(Condition.builder(User.class).andWhere(Sqls.custom().
                andEqualTo(User.FIELD_PHONE, user.getPhone()).andEqualTo(User.FIELD_EMAIL, user.getEmail())).build());
        Assert.notEmpty(users, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        this.rollBackUser(users.get(0));
    }

    /**
     * 回滚用户相关信息
     *
     * @param user 用户信息
     */
    private void rollBackUser(User user) {

        // 回滚用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        BeanUtils.copyProperties(user, userInfo);
        userInfoMapper.delete(userInfo);

        // 回滚成员角色
        user.getRoles();
        MemberRole memberRole = new MemberRole();
        memberRole.setMemberId(user.getId());
        List<MemberRole> memberRoles = memberRoleRepository.select(memberRole);
        if (CollectionUtils.isNotEmpty(memberRoles)) {
            memberRoleRepository.batchDelete(memberRoles);
        }

        // 回滚用户设置
        List<UserConfig> userConfigs = userConfigRepository.selectByCondition(Condition.builder(UserConfig.class).andWhere(
                Sqls.custom().andEqualTo(UserConfig.FIELD_USER_ID, user.getId())).build());
        userConfigRepository.batchDelete(userConfigs);

        redisHelper.hshDelete("hiam:user", user.getId().toString());

        hiamUserRepository.delete(user);
    }

    /**
     * 角色名转换为id
     *
     * @param user 子账户
     */
    private void initMemberRole(UserDTO user, Map<Long, List<User>> adminUserMap) {
        List<User> adminUsers = adminUserMap.get(user.getOrganizationId());
        Assert.isTrue(CollectionUtils.isNotEmpty(adminUsers), IamConstants.ErrorCode.ADMINISTRATOR_HAVE_NOT_INIT);
        DetailsHelper.setCustomUserDetails(adminUsers.get(0).getId(), IamConstants.DEFAULT_LANGUAGE);
        RoleVO roleVO = new RoleVO();

        // 查询可分配的角色
        roleVO.setUserId(adminUsers.get(0).getId());
        List<RoleVO> roleList = roleMapper.selectUserManageableRoles(roleVO);
        Map<String, Long> roleMap = roleList.stream().collect(Collectors.toMap(RoleVO::getName, RoleVO::getId, (key1, key2) -> key2));
        user.getMemberRoleList().forEach(role -> {
            if (role.getAssignLevel() == null) {
                role.setAssignLevel(IamConstants.DEFAULT_ASSIGN_LEVEL);
            }
            role.setAssignLevelValue(user.getOrganizationId());
            Long assignRoleId = roleMap.get(role.getRoleName());
            Assert.isTrue(assignRoleId != null, IamConstants.ErrorCode.USER_ASSIGNED_ROLE_NOT_EXIST);
            role.setRoleId(assignRoleId);
            role.setSourceId(user.getOrganizationId());
            role.setSourceType(IamConstants.DEFAULT_ASSIGN_LEVEL);
            role.setRoleLevel(IamConstants.DEFAULT_ASSIGN_LEVEL);
        });
    }

    /**
     * 初始化需要更新的子账户
     *
     * @param user    子账户
     * @param userMap 已存在的账户信息
     */
    private void initUpdateUser(UserDTO user, Map<String, UserEs> userEsMap, Map<Long, List<User>> userMap) {
        UserEs userEs = userEsMap.get(user.getEsCode().concat(user.getEsUserId()));
        User originUser = userMap.get(userEs.getUserId()).get(0);
        user.setObjectVersionNumber(originUser.getObjectVersionNumber());
        user.set_token(originUser.get_token());
        user.setLoginName(originUser.getLoginName());
        user.setId(userEs.getUserId());
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
        if (user.getLocked() == null) {
            user.setEnabled(false);
        }
        if (user.getEnabledFlag() == null) {
            user.setEnabledFlag(BaseConstants.Flag.YES);
        }
        // 默认手机已验证 可使用手机号登录
        if (user.getPhoneCheckFlag() == null) {
            user.setPhoneCheckFlag(BaseConstants.Flag.YES);
        }
    }
}
