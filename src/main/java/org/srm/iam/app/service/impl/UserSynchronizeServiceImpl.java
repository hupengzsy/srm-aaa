package org.srm.iam.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.message.entity.Receiver;
import org.hzero.boot.oauth.domain.service.UserPasswordService;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Regexs;
import org.hzero.iam.api.dto.CompanyOuInvorgDTO;
import org.hzero.iam.api.dto.ResponseCompanyOuInvorgDTO;
import org.hzero.iam.api.dto.TenantRoleDTO;
import org.hzero.iam.app.service.UserAuthorityService;
import org.hzero.iam.domain.entity.*;
import org.hzero.iam.domain.repository.RoleRepository;
import org.hzero.iam.domain.repository.TenantRepository;
import org.hzero.iam.domain.repository.UserAuthorityRepository;
import org.hzero.iam.domain.service.UserRoleImportService;
import org.hzero.iam.domain.service.role.MemberRoleAssignService;
import org.hzero.iam.domain.service.user.UserBuildService;
import org.hzero.iam.domain.service.user.UserCheckService;
import org.hzero.iam.infra.common.utils.AssertUtils;
import org.hzero.iam.infra.common.utils.UserUtils;
import org.hzero.iam.infra.constant.Constants;
import org.hzero.iam.infra.constant.HiamMemberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.iam.app.service.UserSynchronizeService;
import org.srm.iam.infra.constant.IamConstants;

import java.util.*;

/**
 * <p>
 * 子账户同步 service 实现
 * </p>
 *
 * @author: shi.yan@hand-china.com 2020/2/14
 */
@Service
public class UserSynchronizeServiceImpl extends UserBuildService implements UserSynchronizeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSynchronizeServiceImpl.class);

    @Value("${hzero.send-message.send-create-user:true}")
    protected boolean createUserSendMessage;
    @Value("${hzero.send-message.create-user:HIAM.CREATE_USER}")
    protected String createUserMessageCode;

    @Autowired
    private UserCheckService userCheckService;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserPasswordService userPasswordService;

    @Autowired
    private MemberRoleAssignService memberRoleAssignService;

    @Autowired
    private MessageClient messageClient;

    @Autowired
    private UserRoleImportService userRoleImportService;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public User synchronizeUser(User user) {
        LOGGER.info("begin synchronize user, user={}", user);
        User existUser = this.autoRegisterUserWhenExist(user);
        if (existUser != null) {
            return existUser;
        } else {
            this.initUser(user);
            this.checkValidity(user);
            this.checkLoginName(user);
            this.generateLoginName(user);
            this.handleBeforePersist(user);
            this.persistUser(user);
            this.handleUserInfo(user);
            this.initMemberRole(user);
            this.initDefaultRole(user);
            this.handleMemberRole(user);
            this.handleUserConfig(user);
            addUserAuthority(user);
            this.createUserPostHandle(user);
            this.cacheUser(user);
            LOGGER.info("end create user, user={}", user);
            return this.successful(user);
        }
    }

    @Override
    protected void checkValidity(User user) {
        if (StringUtils.isBlank(user.getRealName())) {
            throw new CommonException("hiam.warn.user.parameterNotBeNull");
        } else if (StringUtils.isNotBlank(user.getEmail()) && !Regexs.isEmail(user.getEmail())) {
            throw new CommonException("hiam.warn.user.emailFormatIncorrect");
        } else if (StringUtils.isNotBlank(user.getPhone()) && !Regexs.isMobile(user.getInternationalTelCode(), user.getPhone())) {
            throw new CommonException("hiam.warn.user.phoneFormatIncorrect");
        } else if (user.getEndDateActive() != null && user.getEndDateActive().isBefore(user.getStartDateActive())) {
            throw new CommonException("hiam.warn.user.endDateBiggerThenStartDate");
        } else {
            this.checkUserTenant(user);
            this.checkUserAccount(user);
        }
    }

    @Override
    protected void persistUser(User user) {
        userRepository.insertSelective(user);
    }

    @Override
    protected void handleUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        BeanUtils.copyProperties(user, userInfo);
        userRepository.insertUserInfoSelective(userInfo);
    }

    @Override
    protected void handleMemberRole(User user) {
        if (!CollectionUtils.isEmpty(user.getMemberRoleList())) {
            user.getMemberRoleList().forEach((item) -> {
                item.setMemberId(user.getId());
                item.setMemberType(HiamMemberType.USER.toString().toLowerCase());
            });
            memberRoleAssignService.assignMemberRole(user.getMemberRoleList(), true);
        }
    }

    protected void checkUserTenant(User user) {
        CustomUserDetails self = UserUtils.getUserDetails();
        if (user.getOrganizationId() == null) {
            LOGGER.info("create user use self default current tenant id.");
            user.setOrganizationId(self.getTenantId());
        }

        Tenant tenant = tenantRepository.selectByPrimaryKey(user.getOrganizationId());
        if (tenant == null) {
            throw new CommonException("hiam.warn.user.tenantNotFound");
        } else if (tenant.getLimitUserQty() != null && tenant.getLimitUserQty() > 0 && this.userRepository.countTenantUser(tenant.getTenantId()) + 1 > tenant.getLimitUserQty()) {
            throw new CommonException("hiam.error.active_users.reached");
        }
    }

    protected void checkUserAccount(User user) {
        if (user.getPassword() == null) {
            String defaultPassword = this.userPasswordService.getTenantDefaultPassword(user.getOrganizationId());
            if (StringUtils.isBlank(defaultPassword)) {
                throw new CommonException("hiam.warn.pwdPolicy.defaultPasswordNull");
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

    protected void createUserPostHandle(User user) {
        if (this.createUserSendMessage) {
            Map<String, String> params = new HashMap<>(2);
            params.put("loginName", user.getLoginName());
            params.put("password", user.getAnotherPassword());
            Receiver receiver = (new Receiver()).setUserId(user.getId()).setTargetUserTenantId(user.getOrganizationId()).setEmail(user.getEmail()).setPhone(user.getPhone());
            LOGGER.debug("send user create message, messageCode={}, receiver={}, params={}", createUserMessageCode, receiver, params);

            try {
                this.messageClient.async().sendMessage(Constants.SITE_TENANT_ID, this.createUserMessageCode, DetailsHelper.getUserDetails().getLanguage(), Collections.singletonList(receiver), params);
            } catch (Exception var5) {
                LOGGER.error("send message error after create user, ex={}", var5.getMessage());
            }

            super.createUserPostHandle(user);
        }
    }

    /**
     * 初始化角色
     */
    protected void initMemberRole(User user) {

    }

    /*
     * 初始化默认角色
     */
    protected void initDefaultRole(User user) {

    }

    /**
     * 初始化权限
     */
    protected void addUserAuthority(User user) {

    }

}
