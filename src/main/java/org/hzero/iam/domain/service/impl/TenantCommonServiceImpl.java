package org.hzero.iam.domain.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseConstants.Flag;
import org.hzero.iam.app.service.LdapService;
import org.hzero.iam.app.service.PasswordPolicyService;
import org.hzero.iam.app.service.RoleService;
import org.hzero.iam.domain.entity.Group;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.entity.Tenant;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.repository.GroupRepository;
import org.hzero.iam.domain.repository.RoleRepository;
import org.hzero.iam.domain.repository.TenantRepository;
import org.hzero.iam.domain.repository.UserRepository;
import org.hzero.iam.domain.service.TenantCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * <p>
 * 覆盖hzero源码
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/11/26 20:08
 */
@Component
public class TenantCommonServiceImpl implements TenantCommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantCommonServiceImpl.class);
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CodeRuleBuilder codeRuleBuilder;
    @Autowired
    private PasswordPolicyService passwordPolicyService;
    @Autowired
    private LdapService ldapService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;

    public TenantCommonServiceImpl() {}

    @Override
    public Tenant createTenant(Tenant tenant) {
        Assert.notNull(tenant, "error.data_invalid");
        LOGGER.info("create tenant start, tenant = {}", tenant);
        this.validateTenant(tenant);
        this.generateTenantDefault(tenant);
        this.tenantRepository.insertSelective(tenant);
        this.createGroup(tenant, this.groupRepository);
        this.passwordPolicyService.createTenantDefaultPasswordPolicy(tenant.getTenantId(), tenant);
        this.ldapService.createDefaultTenantLdap(tenant);
        this.initTenantManagerRole(tenant);
        return tenant;
    }

    @Override
    public Tenant updateTenant(Long tenantId, Tenant tenant) {
        this.validateTenant(tenant);
        Tenant exist = (Tenant) this.tenantRepository.selectByPrimaryKey(tenantId);
        Assert.notNull(exist, "error.data_not_exists");
        if (StringUtils.isNotEmpty(tenant.getTenantName())) {
            exist.setTenantName(tenant.getTenantName());
        }

        if (tenant.getEnabledFlag() != null) {
            exist.setEnabledFlag(tenant.getEnabledFlag());
        }

        exist.setLimitUserQty(tenant.getLimitUserQty());
        this.tenantRepository.updateByPrimaryKey(exist);
        this.passwordPolicyService.updateTenantDefaultPasswordPolicy(tenant.getTenantId(), tenant);
        this.ldapService.updateDefaultTenantLdap(tenant);
        return exist;
    }

    @Override
    @Async("commonAsyncTaskExecutor")
    public void asyncInitTenantInfo(Tenant tenant) {
        this.initTenantManagerRole(tenant);
        this.passwordPolicyService.createTenantDefaultPasswordPolicy(tenant.getTenantId(), tenant);
        this.ldapService.createDefaultTenantLdap(tenant);
    }

    @Override
    @Async("commonAsyncTaskExecutor")
    public void asyncUpdateTenantInfo(Tenant tenant) {
        this.passwordPolicyService.updateTenantDefaultPasswordPolicy(tenant.getTenantId(), tenant);
        this.ldapService.updateDefaultTenantLdap(tenant);
    }

    protected void validateTenant(Tenant tenant) {
        Tenant param = (new Tenant()).setTenantId(tenant.getTenantId()).setTenantNum(tenant.getTenantNum());
        Assert.isTrue(this.tenantRepository.checkRepeatCount(param) == 0, "error.code_repeat");
    }

    protected void generateTenantDefault(Tenant tenant) {
        if (StringUtils.isBlank(tenant.getTenantNum())) {
            String tenantNum = this.generateCode("HPFM.TENANT", (Map) null);
            tenant.setTenantNum(tenantNum);
        }

        if (tenant.getEnabledFlag() == null) {
            tenant.setEnabledFlag(Flag.YES);
        }

    }

    protected void createGroup(Tenant tenant, GroupRepository groupRepository) {
        Group group = new Group();
        group.setGroupNum(this.generateCode("HPFM.GROUP", (Map) null));
        group.setGroupName(tenant.getTenantName());
        group.setTenantId(tenant.getTenantId());
        group.setSourceKey(tenant.getSourceKey());
        group.setSourceCode((String) ObjectUtils.defaultIfNull(tenant.getSourceCode(), "HZERO"));
        group.setEnabledFlag(tenant.getEnabledFlag());
        groupRepository.insertSelective(group);
    }

    protected void initTenantManagerRole(Tenant tenant) {
        LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>租户创建初始化<<<<<<<<<<<<<<<<<<<<\n tenant num is {},tenant name is {}",
                        tenant.getTenantNum(), tenant.getTenantName());
        Role parentRole = new Role();
        parentRole.setCode("role/organization/default/administrator");
        parentRole = (Role) this.roleRepository.selectOne(parentRole);
        User sampleUser = new User();
        sampleUser.setId(BaseConstants.ANONYMOUS_USER_ID);
        User anonymousUser = (User) this.userRepository.selectByPrimaryKey(sampleUser);
        if (anonymousUser == null) {
            throw new CommonException("hiam.warn.anonymousUserNotFound", new Object[0]);
        } else {
            Tenant newTenant = new Tenant();
            BeanUtils.copyProperties(tenant, newTenant);
            this.roleService.createRoleByRoleTpl(anonymousUser, newTenant, parentRole,
                            "role/organization/default/template/administrator");
        }
    }

    private String generateCode(String ruleCode, Map<String, String> variableMap) {
        return this.codeRuleBuilder.generateCode("T", BaseConstants.DEFAULT_TENANT_ID, ruleCode, "GLOBAL", "GLOBAL",
                        variableMap);
    }
}
