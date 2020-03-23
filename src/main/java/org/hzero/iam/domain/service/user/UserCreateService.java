package org.hzero.iam.domain.service.user;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.message.entity.Attachment;
import org.hzero.boot.message.entity.Receiver;
import org.hzero.boot.oauth.domain.service.UserPasswordService;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseConstants.Flag;
import org.hzero.core.util.Regexs;
import org.hzero.iam.api.dto.TenantRoleDTO;
import org.hzero.iam.domain.entity.Tenant;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.entity.UserInfo;
import org.hzero.iam.domain.repository.RoleRepository;
import org.hzero.iam.domain.repository.TenantRepository;
import org.hzero.iam.domain.repository.UserConfigRepository;
import org.hzero.iam.domain.service.role.MemberRoleAssignService;
import org.hzero.iam.infra.common.utils.UserUtils;
import org.hzero.iam.infra.constant.Constants;
import org.hzero.iam.infra.constant.HiamMemberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.srm.iam.domain.repository.PortalAssignRepository;
import org.srm.iam.domain.vo.PortalAssignVO;

/**
 * description
 *
 * @author mingzhi.li 2020/02/16 14:41
 */
public class UserCreateService extends UserBuildService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(UserCreateService.class);
    private static final String DOMAIN_NAME_NOT_NULL = "domain.name.not.null";
    private static final String REGISTER_URL_HEAD = "http://";
    private static final String REGISTER_URL_END = "/oauth/public/default/register.html";
    @Autowired
    protected UserConfigRepository userConfigRepository;
    @Autowired
    protected MessageClient messageClient;
    @Autowired
    protected UserCheckService userCheckService;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected TenantRepository tenantRepository;
    @Autowired
    protected MemberRoleAssignService memberRoleAssignService;
    @Autowired
    protected UserPasswordService userPasswordService;
    @Autowired
    private PortalAssignRepository portalAssignRepository;
    @Value("${hzero.send-message.create-user:HIAM.CREATE_USER}")
    protected String createUserMessageCode;
    @Value("${hzero.send-message.send-create-user:true}")
    protected boolean createUserSendMessage;

    public UserCreateService() {
    }

    public User createUser(User user) {
        return super.createUser(user);
    }

    protected final User updateUser(User user) {
        return super.updateUser(user);
    }

    protected final User registerUser(User user) {
        return super.registerUser(user);
    }

    protected void checkValidity(User user) {
        if (StringUtils.isBlank(user.getRealName())) {
            throw new CommonException("hiam.warn.user.parameterNotBeNull", new Object[]{"realName"});
        } else if (StringUtils.isNotBlank(user.getEmail()) && !Regexs.isEmail(user.getEmail())) {
            throw new CommonException("hiam.warn.user.emailFormatIncorrect", new Object[0]);
        } else if (StringUtils.isNotBlank(user.getPhone()) && !Regexs.isMobile(user.getInternationalTelCode(), user.getPhone())) {
            throw new CommonException("hiam.warn.user.phoneFormatIncorrect", new Object[0]);
        } else if (user.getEndDateActive() != null && user.getEndDateActive().isBefore(user.getStartDateActive())) {
            throw new CommonException("hiam.warn.user.endDateBiggerThenStartDate", new Object[0]);
        } else {
            this.checkUserTenant(user);
            this.checkUserAccount(user);
            this.checkUserMemberRole(user);
        }
    }

    protected void checkUserTenant(User user) {
        CustomUserDetails self = UserUtils.getUserDetails();
        if (user.getOrganizationId() == null) {
            LOGGER.info("create user use self default current tenant id.");
            user.setOrganizationId(self.getTenantId());
        }

        Tenant tenant = (Tenant)this.tenantRepository.selectByPrimaryKey(user.getOrganizationId());
        if (tenant == null) {
            throw new CommonException("hiam.warn.user.tenantNotFound", new Object[0]);
        } else if (tenant.getLimitUserQty() != null && tenant.getLimitUserQty() > 0 && this.userRepository.countTenantUser(tenant.getTenantId()) + 1 > tenant.getLimitUserQty()) {
            throw new CommonException("hiam.error.active_users.reached", new Object[0]);
        }
    }

    protected void checkUserAccount(User user) {
        if (user.getPassword() == null) {
            String defaultPassword = this.userPasswordService.getTenantDefaultPassword(user.getOrganizationId());
            if (StringUtils.isBlank(defaultPassword)) {
                throw new CommonException("hiam.warn.pwdPolicy.defaultPasswordNull", new Object[0]);
            }

            user.setPassword(defaultPassword);
            user.setAnotherPassword(defaultPassword);
        } else if (user.getCheckPasswordPolicy() == null || user.getCheckPasswordPolicy()) {
            this.userCheckService.checkPasswordPolicy(user.getPassword(), user.getOrganizationId());
            user.setAnotherPassword(user.getPassword());
        }

        if (StringUtils.isNotBlank(user.getPhone())) {
            this.userCheckService.checkPhoneRegistered(user.getPhone());
        }

        if (StringUtils.isNotBlank(user.getEmail())) {
            this.userCheckService.checkEmailRegistered(user.getEmail());
        }

    }

    protected void checkUserMemberRole(User user) {
        if (CollectionUtils.isEmpty(user.getMemberRoleList())) {
            throw new CommonException("hiam.warn.user.assignLeastOneRole", new Object[0]);
        } else {
            Map<Long, List<TenantRoleDTO>> map = (Optional.ofNullable(user.getDefaultRoles()).orElse(Collections.emptyList())).stream().filter((t) -> {
                return Objects.equals(t.getDefaultFlag(), Flag.YES);
            }).collect(Collectors.groupingBy(TenantRoleDTO::getTenantId));
            map.forEach((k, v) -> {
                if (v.size() > 1) {
                    throw new IllegalArgumentException("Only one default role is allowed under the same tenant.");
                }
            });
        }
    }

    protected void persistUser(User user) {
        this.userRepository.insertSelective(user);
    }

    protected void handleUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        BeanUtils.copyProperties(user, userInfo);
        this.userRepository.insertUserInfoSelective(userInfo);
    }

    protected void handleMemberRole(User user) {
        if (!CollectionUtils.isEmpty(user.getMemberRoleList())) {
            user.getMemberRoleList().forEach((item) -> {
                item.setMemberId(user.getId());
                item.setMemberType(HiamMemberType.USER.toString().toLowerCase());
            });
            this.memberRoleAssignService.assignMemberRole(user.getMemberRoleList(), true);
        }
    }

    protected void createUserPostHandle(User user) {
        //查询租户下的门户分配信息
        String registerUrlMid;
        List<PortalAssignVO> list = portalAssignRepository.select(PortalAssignVO.FIELD_TENANT_ID, DetailsHelper.getUserDetails().getTenantId());
        PortalAssignVO originPortalAssign = null;
        if (!ObjectUtils.isEmpty(list)) {
            originPortalAssign = list.get(0);
        }
        if (originPortalAssign == null) {
            PortalAssignVO portalAssign = new PortalAssignVO();
            portalAssign.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
            originPortalAssign = portalAssignRepository.selectOne(portalAssign);
            Assert.isTrue(!(originPortalAssign == null || ObjectUtils.isEmpty(originPortalAssign.getWebUrl())), DOMAIN_NAME_NOT_NULL);
        } else {
            Assert.isTrue(!ObjectUtils.isEmpty(originPortalAssign.getWebUrl()), DOMAIN_NAME_NOT_NULL);
        }
        registerUrlMid = originPortalAssign.getWebUrl();
        String projectSite = registerUrlMid.substring(BaseConstants.Digital.ZERO ,registerUrlMid.indexOf(BaseConstants.Symbol.POINT));
        if (this.createUserSendMessage) {
            Map<String, String> params = new HashMap(4);
            params.put("loginName", user.getLoginName());
            params.put("password", user.getAnotherPassword());
            params.put("tenantName", tenantRepository.selectByPrimaryKey(user.getOrganizationId()).getTenantName());
            params.put("subdomain",projectSite);
            Receiver receiver = (new Receiver()).setUserId(user.getId()).setTargetUserTenantId(user.getOrganizationId()).setEmail(user.getEmail()).setPhone(user.getPhone());
            LOGGER.debug("send user create message, messageCode={}, receiver={}, params={}", new Object[]{this.createUserMessageCode, receiver, params});

            try {
                this.messageClient.async().sendMessage(user.getOrganizationId() == null ? Constants.SITE_TENANT_ID : user.getOrganizationId(), this.createUserMessageCode, (String)null, Collections.singletonList(receiver), params, new Attachment[0]);
            } catch (Exception var5) {
                LOGGER.error("send message error after create user, ex={}", var5.getMessage());
            }

            super.createUserPostHandle(user);
        }
    }
}
