package org.hzero.iam.domain.service.user;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.common.UserSource;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Regexs;
import org.hzero.iam.app.service.MemberRoleService;
import org.hzero.iam.domain.entity.MemberRole;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.entity.UserInfo;
import org.hzero.iam.domain.repository.MemberRoleRepository;
import org.hzero.iam.domain.repository.RoleRepository;
import org.hzero.iam.domain.repository.UserConfigRepository;
import org.hzero.iam.infra.constant.Constants;
import org.hzero.iam.infra.constant.HiamResourceLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.iam.domain.entity.UserPortalEs;
import org.srm.iam.domain.repository.UserPortalEsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户注册service 覆盖H0jar包
 * </p>
 *
 * @author: shi.yan@hand-china.com 2020/2/24
 */
public class UserRegisterService extends UserBuildService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserUpdateService.class);
    @Autowired
    protected MemberRoleService memberRoleService;
    @Autowired
    protected MemberRoleRepository memberRoleRepository;
    @Autowired
    protected UserConfigRepository userConfigRepository;
    @Autowired
    protected UserCheckService userCheckService;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    private UserPortalEsRepository userPortalEsRepository;
    @Autowired
    private HttpServletRequest request;


    public UserRegisterService() {
    }

    protected final User createUser(User user) {
        return super.createUser(user);
    }

    protected final User updateUser(User user) {
        return super.updateUser(user);
    }

    public User registerUser(User user) {
        LOGGER.info("begin register user, user={}", user);
        User existUser = this.autoRegisterUserWhenExist(user);
        if (existUser != null) {
            return existUser;
        } else {
            this.initUser(user);
            this.checkValidity(user);
            this.generateLoginName(user);
            this.handleBeforePersist(user);
            this.persistUser(user);
            this.handleUserInfo(user);
            this.handleMemberRole(user);
            this.handleUserConfig(user);
            this.handleUserPortal(user);
            this.createUserPostHandle(user);
            this.cacheUser(user);
            LOGGER.info("end register user, user={}", user);
            return this.successful(user);
        }
    }

    protected void initUser(User user) {
        user.setPasswordResetFlag(BaseConstants.Flag.YES);
        user.setUserSource(UserSource.SELF_REGISTER.code());
        if (user.getStartDateActive() == null) {
            user.setStartDateActive(LocalDate.now());
        }

        user.setOrganizationId(Constants.SITE_TENANT_ID);
    }

    protected void checkValidity(User user) {
        if (StringUtils.isBlank(user.getRealName())) {
            throw new CommonException("hiam.warn.user.parameterNotBeNull", "realName");
        } else if (StringUtils.isBlank(user.getPassword())) {
            throw new CommonException("hiam.warn.user.parameterNotBeNull", "password");
        } else if (StringUtils.isNotBlank(user.getEmail()) && !Regexs.isEmail(user.getEmail())) {
            throw new CommonException("hiam.warn.user.emailFormatIncorrect");
        } else if (StringUtils.isNotBlank(user.getPhone()) && !Regexs.isMobile(user.getInternationalTelCode(), user.getPhone())) {
            throw new CommonException("hiam.warn.user.phoneFormatIncorrect");
        } else {
            this.checkUserAccount(user);
        }
    }

    protected void checkUserAccount(User user) {
        if (StringUtils.isNotBlank(user.getCompanyName())) {
            this.userCheckService.checkCompanyNameRegistered(user.getCompanyName());
        }

        if (StringUtils.isNotBlank(user.getPhone())) {
            this.userCheckService.checkPhoneRegistered(user.getPhone());
        }

        if (StringUtils.isNotBlank(user.getEmail())) {
            this.userCheckService.checkEmailRegistered(user.getEmail());
        }

        this.userCheckService.checkPasswordPolicy(user.getPassword(), user.getOrganizationId());
        if (user.getTextId() != null) {
            this.userCheckService.checkRegistrationProtocol(user.getTextId());
        }

    }

    protected void persistUser(User user) {
        this.userRepository.insertSelective(user);
    }

    protected void handleUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        userInfo.setUserId(user.getId());
        this.userRepository.insertUserInfoSelective(userInfo);
    }

    protected void handleMemberRole(User user) {
        List<MemberRole> memberRoleList = new ArrayList();
        Role siteGuestRole = this.roleRepository.selectRoleSimple("role/site/default/guest");
        if (siteGuestRole != null) {
            memberRoleList.add(new MemberRole(siteGuestRole.getId(), user.getId(), "user", Constants.SITE_TENANT_ID, HiamResourceLevel.SITE.value(), HiamResourceLevel.ORGANIZATION.value(), Constants.SITE_TENANT_ID));
        }

        Role organizationGuestRole = this.roleRepository.selectRoleSimple("role/organization/default/guest");
        if (organizationGuestRole != null) {
            memberRoleList.add(new MemberRole(organizationGuestRole.getId(), user.getId(), "user", Constants.SITE_TENANT_ID, HiamResourceLevel.ORGANIZATION.value(), HiamResourceLevel.ORGANIZATION.value(), Constants.SITE_TENANT_ID));
        }

        LOGGER.debug("generate default guest role for register user, memberRoles={}", memberRoleList);
        if (CollectionUtils.isNotEmpty(memberRoleList)) {
            this.memberRoleRepository.batchInsertSelective(memberRoleList);
            user.setMemberRoleList(memberRoleList);
        }

    }

    private void handleUserPortal(User user) {
        String webUrl = StringUtils.substringBetween(request.getHeader("referer"), BaseConstants.Symbol.DOUBLE_SLASH, BaseConstants.Symbol.SLASH);
        if (webUrl != null) {
            Long tenantId = userPortalEsRepository.selectTenantIdByWebUrl(webUrl);
            if (tenantId != null && !tenantId.equals(BaseConstants.DEFAULT_TENANT_ID)) {
                UserPortalEs userPortalEs = new UserPortalEs(user.getId(), tenantId);
                userPortalEsRepository.insert(userPortalEs);
            }
        }
    }
}