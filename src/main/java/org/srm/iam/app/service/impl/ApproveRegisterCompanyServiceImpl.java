package org.srm.iam.app.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.validator.ValidList;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.pack.omega.transaction.annotations.Compensable;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.iam.api.dto.CompanyOuInvorgDTO;
import org.hzero.iam.api.dto.TenantAdminRoleAndDataPrivAutoAssignmentDTO;
import org.hzero.iam.api.dto.UserAuthorityDTO;
import org.hzero.iam.app.service.MemberRoleExternalService;
import org.hzero.iam.app.service.RoleAuthDataService;
import org.hzero.iam.app.service.TenantService;
import org.hzero.iam.app.service.UserAuthorityService;
import org.hzero.iam.domain.entity.*;
import org.hzero.iam.domain.repository.*;
import org.hzero.iam.domain.vo.UserVO;
import org.hzero.iam.infra.constant.HiamMemberType;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.srm.boot.saga.infra.annotation.SagaCompensate;
import org.srm.boot.saga.utils.SagaClient;
import org.srm.iam.api.dto.TenantRoleAutoAssignmentDTO;
import org.srm.iam.app.service.ApproveRegisterCompanyService;
import org.srm.iam.domain.repository.ApproveRegisterCompanyRepository;
import org.srm.iam.domain.vo.SslmCompanyVO;
import org.srm.iam.infra.constant.IamConstants;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自动审批实现
 *
 * @author peng.yang03@hand-china.com 2019/08/01 10:52
 */
@Service
public class ApproveRegisterCompanyServiceImpl implements ApproveRegisterCompanyService {

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    MemberRoleExternalService hiamMemberRoleExternalService;
    @Autowired
    private RoleAuthDataService roleAuthDataService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GroupRepository hiamGroupRepository;
    @Autowired
    private RoleRepository hiamRoleRepository;
    @Autowired
    private TenantRepository hiamOrganizationRepository;
    @Autowired
    private PasswordPolicyRepository passwordPolicyRepository;
    @Autowired
    private LdapRepository ldapRepository;
    @Autowired
    private UserRepository hiamUserRepository;
    @Autowired
    private RoleAuthDataLineRepository roleAuthDataLineRepository;
    @Autowired
    private RolePermissionRepository hiamRolePermissionRepository;
    @Autowired
    private MemberRoleRepository hiamMemberRoleRepository;
    @Autowired
    private UserConfigRepository userConfigRepository;
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    @Autowired
    private UserAuthorityLineRepository userAuthorityLineRepository;
    @Autowired
    private ApproveRegisterCompanyRepository approveRegisterCompanyRepository;
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private UserAuthorityService userAuthorityService;

    private static final Logger logger = LoggerFactory.getLogger(ApproveRegisterCompanyServiceImpl.class);

    private static final String SAGA_METHOD_NAME = "HIAM-APPROVEREGISTERCOMPANYSERVICEIMPL-APPROVEREGISTERCOMPANY";
    private static final String SAGA_METHOD_NAME_AUTHORITY =
                    "HIAM-APPROVEREGISTERCOMPANYSERVICEIMPL-CREATETREEUSERAUTHORITYANDASSIGNROLE";

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Compensable(compensationMethod = "approveRegisterCompanyRollback")
    public Map approveRegisterCompany(String sagaKey, SslmCompanyVO companyVO) {
        // 幂等数据处理
        Map<String, String> sagaMap = new HashMap<>(8);
        try {
            String companyString = objectMapper.writeValueAsString(companyVO);
            Assert.notNull(companyString, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            String userString = objectMapper
                            .writeValueAsString(hiamUserRepository.selectByPrimaryKey(companyVO.getCreatedBy()));
            Assert.notNull(userString, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            sagaMap.put("UPDATE-COMPANY-1", companyString);
            sagaMap.put("UPDATE-USER-1", userString);
        } catch (Exception e) {
            throw new CommonException(IamConstants.ErrorCode.SAGA_INIT_ERROR);
        }
        // 幂等数据缓存
        SagaClient.putSagaData(sagaKey, SAGA_METHOD_NAME, sagaMap);

        // 处理公司信息 有租户 直接为companyVO设置租户信息 无租户 创建租户后为companyVO设置租户信息
        Tenant tenant = this.registerOrRefreshModify(companyVO);
        // 查询用户详情
        UserVO userVO = this.getUserDetail(companyVO);

        Map<String, Object> map = new HashMap<>(4);
        map.put("user", userVO);
        map.put("tenant", tenant);
        map.put("company", companyVO);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Compensable(compensationMethod = "createTreeUserAuthorityAndAssignRoleRollback")
    public void createTreeUserAuthorityAndAssignRole(String sagaKey, SslmCompanyVO companyVO) {
        HashMap<String, String> sagaMap = new HashMap<>(2);

        // 如果是新建租户 为角色分配默认权限
        if (BaseConstants.Flag.YES.equals(companyVO.getNewTenantFlag())) {

            // 幂等数据处理
            try {
                // user
                User user = hiamUserRepository.selectByPrimaryKey(companyVO.getCreatedBy());
                String userString = objectMapper.writeValueAsString(user);
                Assert.notNull(userString, BaseConstants.ErrorCode.DATA_INVALID);
                sagaMap.put("UPDATE-USER-2", userString);

                // guest member role
                String[] guestRoleCodeArray =
                                new String[] {"role/site/default/guest", "role/organization/default/guest"};
                List<MemberRole> guestMemberRoles = new ArrayList<>(2);
                for (String guestRoleCode : guestRoleCodeArray) {
                    Role queryRole = new Role();
                    queryRole.setCode(guestRoleCode);
                    Role guestRole = hiamRoleRepository.selectOne(queryRole);
                    MemberRole queryMemberRole =
                                    new MemberRole(guestRole.getId(), user.getId(), HiamMemberType.USER.value());
                    MemberRole guestMemberRole = hiamMemberRoleRepository.selectOne(queryMemberRole);
                    guestMemberRoles.add(guestMemberRole);
                }
                String guestMemberRolesString = objectMapper.writeValueAsString(guestMemberRoles);
                Assert.notNull(guestMemberRolesString, BaseConstants.ErrorCode.DATA_INVALID);
                sagaMap.put("DELETE-GUESTMEMBERROLE-1", guestMemberRolesString);

                // 原有业务逻辑
                TenantAdminRoleAndDataPrivAutoAssignmentDTO dto = new TenantAdminRoleAndDataPrivAutoAssignmentDTO();
                this.initTenantAdminRoleAndDataPriv(dto, companyVO);
                hiamMemberRoleExternalService.batchAutoAssignTenantAdminRoleAndDataPriv(new ValidList<>(Collections.singletonList(dto)));
            } catch (Exception e) {
                throw new CommonException(IamConstants.ErrorCode.SAGA_INIT_ERROR);
            }
        }else{
            // 将企业添加到提交人企业维度下
            this.createTreeUserAuthority(companyVO);
        }

        // 幂等数据处理
        // company
        try {
            String companyString = objectMapper.writeValueAsString(companyVO);
            Assert.notNull(companyString, BaseConstants.ErrorCode.DATA_INVALID);
            sagaMap.put("UPDATE-COMPANY-2", companyString);
        } catch (Exception e) {
            throw new CommonException(IamConstants.ErrorCode.SAGA_INIT_ERROR);
        }

        // 幂等数据缓存
        SagaClient.putSagaData(sagaKey, SAGA_METHOD_NAME_AUTHORITY, sagaMap);
    }

    /**
     * 处理公司信息
     *
     * @param companyVO 公司信息
     */
    private Tenant registerOrRefreshModify(SslmCompanyVO companyVO) {
        // 创建人租户查询
        Tenant tenant = approveRegisterCompanyRepository.getTenantByUserId(companyVO.getCreatedBy());

        // 租户处理
        if (tenant == null) {
            // 有租户id 用租户id查询租户
            if (companyVO.getTenantId() != null) {
                tenant = tenantRepository.selectByPrimaryKey(companyVO.getTenantId());
            }
            // 没有 新建
            if (tenant == null) {
                tenant = this.createTenantModify(companyVO);
                companyVO.setNewTenantFlag(BaseConstants.Flag.YES);
            } else {
                companyVO.setNewTenantFlag(BaseConstants.Flag.NO);
            }
            companyVO.setTenantId(tenant.getTenantId());

            // 为用户分配租户
            int assignCount = tenantService.assignTenantToUser(tenant.getTenantId(), companyVO.getCreatedBy());
            Assert.isTrue(assignCount > 0, IamConstants.ErrorCode.COMPANY_REGISTER_TENANT_ASSIGN_ERROR);
        } else {
            companyVO.setNewTenantFlag(BaseConstants.Flag.NO);
            companyVO.setTenantId(tenant.getTenantId());
        }

        return tenant;
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param companyVO 公司
     * @return 用户信息
     */
    private UserVO getUserDetail(SslmCompanyVO companyVO) {
        UserVO userVO = new UserVO();
        userVO.setId(companyVO.getCreatedBy());
        return userRepository.selectUserDetails(userVO);
    }

    /**
     * 企业认证审批通过后自动将企业添加到提交人企业维度下
     *
     * @param company 公司信息
     */
    private void createTreeUserAuthority(SslmCompanyVO company) {
        //创建一个分配权限需要的dto，一个权限头，一个权限行集合
        UserAuthorityDTO userAuthorityDTO = new UserAuthorityDTO();
        UserAuthority userAuthorityTemp = new UserAuthority();
        UserAuthorityLine userAuthorityLine = new UserAuthorityLine();
        List<UserAuthorityLine> UserAuthorityLineList = new ArrayList<>();
        //设定权限头参数
        userAuthorityTemp.setIncludeAllFlag(0);
        userAuthorityTemp.setTenantId(company.getTenantId());
        userAuthorityTemp.setUserId(company.getCreatedBy());
        userAuthorityTemp.setAuthorityTypeCode(IamConstants.OrgTypeCode.COMPANY);
        //设定权限行参数
        userAuthorityLine.setDataId(company.getRemoteCompanyId());
        userAuthorityLine.setTenantId(company.getTenantId());
        userAuthorityLine.setDataCode(company.getCompanyNum());
        userAuthorityLine.setDataName(company.getCompanyName());
        //加入集合，设定权限dto
        UserAuthorityLineList.add(userAuthorityLine);
        userAuthorityDTO.setUserAuthority(userAuthorityTemp);
        userAuthorityDTO.setUserAuthorityLineList(UserAuthorityLineList);
        //查询权限头
        List<UserAuthority> select = userAuthorityRepository.select(userAuthorityTemp);
        if(!CollectionUtils.isEmpty(select)) {
            //若存在就将dto中权限头替换为查询出来的
            userAuthorityDTO.setUserAuthority(select.get(0));
        }

        //若不存在权限就将调用原有方法，完成权限分配
        UserAuthorityDTO userAuthority = userAuthorityService.batchCreateUserAuthority(company.getTenantId(), company.getCreatedBy(), IamConstants.OrgTypeCode.COMPANY,userAuthorityDTO );
        Assert.notNull(userAuthority, IamConstants.ErrorCode.CREATE_ROLE_AUTHORITY_ERROR);
    }

    /**
     * 创建租户
     *
     * @param companyVO 公司信息
     * @return 租户
     */
    private Tenant createTenantModify(SslmCompanyVO companyVO) {
        Tenant tenant = this.initTenant(companyVO);
        tenantService.createTenant(tenant);
        Assert.notNull(tenant.getTenantId(), IamConstants.ErrorCode.COMPANY_REGISTER_TENANT_CREATE_ERROR);
        companyVO.setTenantId(tenant.getTenantId());
        return tenant;
    }

    /**
     * 根据公司信息 初始化租户数据
     *
     * @param companyVO 公司信息
     * @return 初始化租户
     */
    private Tenant initTenant(SslmCompanyVO companyVO) {
        Assert.notNull(companyVO, BaseConstants.ErrorCode.DATA_INVALID);
        Assert.notNull(companyVO.getCompanyName(), BaseConstants.ErrorCode.DATA_INVALID);
        Tenant tenant = new Tenant();
        tenant.setTenantId(companyVO.getTenantId());
        tenant.setSourceKey(companyVO.getSourceKey());
        tenant.setTenantName(companyVO.getCompanyName());
        tenant.setEnabledFlag(companyVO.getEnabledFlag());
        tenant.setSourceCode(IamConstants.SRM_SOURCE_CODE);

        return tenant;
    }

    /**
     * 初始化公司信息
     *
     * @param company 公司参数
     * @return 公司
     */
    private CompanyOuInvorgDTO initCompanyOuDTO(SslmCompanyVO company) {
        CompanyOuInvorgDTO companyOuDTO = new CompanyOuInvorgDTO();
        companyOuDTO.setTypeCode(IamConstants.OrgTypeCode.COMPANY);
        companyOuDTO.setDataId(company.getRemoteCompanyId());
        companyOuDTO.setDataCode(company.getCompanyNum());
        companyOuDTO.setDataName(company.getCompanyName());

        return companyOuDTO;
    }

    /**
     * 初始化默认角色权限
     *
     * @param dto 角色权限dto
     * @param company 公司信息
     */
    private void initTenantAdminRoleAndDataPriv(TenantAdminRoleAndDataPrivAutoAssignmentDTO dto,
                    SslmCompanyVO company) {

        dto.setUserId(company.getCreatedBy());
        dto.setTenantId(company.getTenantId());
        dto.setNewTenantFlag(Objects.equals(company.getNewTenantFlag(), BaseConstants.Flag.YES));
        dto.setCompanyNum(company.getCompanyNum());
        dto.setSourceKey(company.getSourceKey());
        dto.setSourceCode(company.getSourceCode());

        List<String> roleTemplateCodes = new ArrayList<>();
        roleTemplateCodes.add(TenantRoleAutoAssignmentDTO.RoleTemplateCode.DEFAULT_SALER_ADMIN.getCode());
        roleTemplateCodes.add(TenantRoleAutoAssignmentDTO.RoleTemplateCode.DEFAULT_EXPERT.getCode());
        dto.setRoleTemplateCodes(roleTemplateCodes);
    }

    /**
     * 注册公司审批的saga补偿方法
     *
     * @param sagaKey sagaKey
     * @param company 公司信息
     * @throws Exception 补偿方法不能加@Transactional注解
     */
    @SagaCompensate(methodKey = SAGA_METHOD_NAME)
    public void approveRegisterCompanyRollback(String sagaKey, SslmCompanyVO company) throws Exception {
        // 查询需要回滚的数据
        Map<String, String> readSagaMap = SagaClient.getSagaData(sagaKey, SAGA_METHOD_NAME);
        SslmCompanyVO originCompanyVO =
                        objectMapper.readValue(readSagaMap.get("UPDATE-COMPANY-1"), SslmCompanyVO.class);
        logger.info("before if company VO{}", originCompanyVO);
        if (null == originCompanyVO.getTenantId()) {
            logger.info("after if company VO {}", originCompanyVO);
            logger.info("after if tenant id{}", originCompanyVO.getTenantId());
            logger.info("after if company name {}", originCompanyVO.getCompanyName());
            User originUser = objectMapper.readValue(readSagaMap.get("UPDATE-USER-1"), User.class);

            // 需要回滚的租户相关数据 1.租户 2.集团 3.角色 4.组织 5.密码策略 6.ladp 7.密码策略缓存
            // 权限分配需要回滚的数据 提交人企业维度

            // 获取租户信息
            List<Tenant> tenants = tenantRepository.selectByCondition(Condition.builder(Tenant.class)
                    .andWhere(Sqls.custom().andEqualTo(Tenant.TENANT_NAME, originCompanyVO.getCompanyName()))
                    .build());
            Assert.notEmpty(tenants, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            Tenant tenant = tenants.get(0);

            // 获取组织信息
            Tenant organization = hiamOrganizationRepository.selectByPrimaryKey(tenant.getTenantId());
            Assert.notNull(organization, BaseConstants.ErrorCode.DATA_NOT_EXISTS);

            // 获取PasswordPolicy
            List<PasswordPolicy> passwordPolicies =
                    passwordPolicyRepository.select("organizationId", organization.getTenantId());
            // 删除redis中缓存
            if (!CollectionUtils.isEmpty(passwordPolicies)) {
                String uniqueKey = StringUtils.join("hoth", ":", "password_policy");
                PasswordPolicy passwordPolicyToDelete = passwordPolicies.get(0);
                redisHelper.hshDelete(uniqueKey, passwordPolicyToDelete.getOrganizationId().toString());
                // 删除数据库中记录
                passwordPolicyRepository.delete(passwordPolicies.get(0));
            }

            // 获取ldap
            Ldap ldap = ldapRepository.selectLdapByTenantId(organization.getTenantId());
            if (null != ldap) {
                ldapRepository.delete(ldap);
            }

            // 删除租户
            tenantRepository.delete(tenant);

            hiamOrganizationRepository.delete(organization);

            // 获取集团信息
            List<Group> groups = hiamGroupRepository.select(Group.FIELD_TENANT_ID, tenant.getTenantId());
            if (groups != null) {
                hiamGroupRepository.delete(groups.get(0));
            }

            // 恢复用户的租户
            hiamUserRepository.updateOptional(originUser, User.FIELD_ORGANIZATION_ID);
        }
    }


    /**
     * 企业注册审批用户角色权限分配补偿方法
     *
     * @param sagaKey sagaKey
     * @param companyVO 公司信息
     * @throws IOException 反序列化IOException
     */
    @SagaCompensate(methodKey = SAGA_METHOD_NAME_AUTHORITY)
    public void createTreeUserAuthorityAndAssignRoleRollback(String sagaKey, SslmCompanyVO companyVO)
                    throws IOException {
        // 从Saga缓存中反序列化数据company
        Map<String, String> readSagaMap = SagaClient.getSagaData(sagaKey, SAGA_METHOD_NAME_AUTHORITY);
        SslmCompanyVO originCompany = objectMapper.readValue(readSagaMap.get("UPDATE-COMPANY-2"), SslmCompanyVO.class);

        if (BaseConstants.Flag.YES.equals(originCompany.getNewTenantFlag())) {
            // 从Saga缓存反序列化数据user guest_member_role
            User originUser = objectMapper.readValue(readSagaMap.get("UPDATE-USER-2"), User.class);

            // 构建List<MemberRole>类型用于反序列化
            JavaType memberRoleList =
                            objectMapper.getTypeFactory().constructCollectionType(List.class, MemberRole.class);
            List<MemberRole> guestMemberRoles =
                            objectMapper.readValue(readSagaMap.get("DELETE-GUESTMEMBERROLE-1"), memberRoleList);

            Long tenantId = originCompany.getTenantId();
            List<Role> rolesToDelete = hiamRoleRepository.select(Role.FIELD_TENANT_ID, tenantId);

            // 恢复role_permission，根据角色id删除
            List<Long> roleIds = rolesToDelete.stream().map(Role::getId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(roleIds)) {
                List<RolePermission> rolePermissionsToDelete = hiamRolePermissionRepository.selectByCondition(Condition
                                .builder(RolePermission.class)
                                .andWhere(Sqls.custom().andIn(RolePermission.FIELD_ROLE_ID, roleIds)).build());
                hiamRolePermissionRepository.batchDelete(rolePermissionsToDelete);
            }

            // 恢复role，根据租户id删除
            hiamRoleRepository.batchDelete(rolesToDelete);


            // 恢复member_role，删除新增记录，插入游客记录
            List<MemberRole> memberRolesToDelete =
                            hiamMemberRoleRepository.select(MemberRole.FIELD_MEMBER_ID, originUser.getId());
            hiamMemberRoleRepository.batchDelete(memberRolesToDelete);
            hiamMemberRoleRepository.batchInsert(guestMemberRoles);

            // 恢复user_config，删除新增记录
            UserConfig userConfigOptions = new UserConfig(originUser.getId(), originCompany.getTenantId());
            UserConfig userConfigToDelete = userConfigRepository.selectOne(userConfigOptions);
            userConfigRepository.delete(userConfigToDelete);

            // 恢复user_authority和user_authority_line，删除新增记录
            UserAuthority userAuthorityOptions = new UserAuthority();
            userAuthorityOptions.setUserId(originUser.getId());
            userAuthorityOptions.setTenantId(originCompany.getTenantId());
            userAuthorityOptions.setAuthorityTypeCode(IamConstants.OrgTypeCode.COMPANY);
            UserAuthority userAuthorityToDelete = userAuthorityRepository.selectOne(userAuthorityOptions);

            UserAuthorityLine userAuthorityLineOptions = new UserAuthorityLine();
            Assert.notNull(userAuthorityToDelete, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            userAuthorityLineOptions.setAuthorityId(userAuthorityToDelete.getAuthorityId());
            userAuthorityLineOptions.setDataId(originCompany.getCompanyId());
            UserAuthorityLine userAuthorityLineToDelete =
                            userAuthorityLineRepository.selectOne(userAuthorityLineOptions);
            // 删除user_authority_line
            userAuthorityLineRepository.delete(userAuthorityLineToDelete);
            // 删除user_authority
            userAuthorityRepository.delete(userAuthorityToDelete);
        }

        // 角色权限 恢复删除的 删除新增的
        List<RoleAuthDataLine> roleAuthDataLines = roleAuthDataLineRepository.selectByCondition(Condition
                        .builder(RoleAuthDataLine.class).andWhere(Sqls.custom()
                                        .andEqualTo(RoleAuthDataLine.FIELD_DATA_CODE, originCompany.getCompanyName()))
                        .build());
        roleAuthDataLineRepository.batchDelete(roleAuthDataLines);

    }
}
