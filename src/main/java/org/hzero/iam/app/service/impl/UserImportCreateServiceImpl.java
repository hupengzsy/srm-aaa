package org.hzero.iam.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.oauth.domain.service.UserPasswordService;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.message.MessageAccessor;
import org.hzero.core.util.Regexs;
import org.hzero.iam.api.dto.TenantRoleDTO;
import org.hzero.iam.app.service.UserImportCreateService;
import org.hzero.iam.domain.entity.Tenant;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.entity.UserInfo;
import org.hzero.iam.domain.repository.TenantRepository;
import org.hzero.iam.domain.repository.UserRepository;
import org.hzero.iam.domain.service.role.MemberRoleAssignService;
import org.hzero.iam.domain.service.user.UserBuildService;
import org.hzero.iam.domain.service.user.UserCheckService;
import org.hzero.iam.infra.common.utils.UserUtils;
import org.hzero.iam.infra.constant.HiamMemberType;
import org.hzero.iam.infra.constant.UserType;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 子账户导入 service实现类
 * </p>
 *
 * @author: shi.yan@hand-china.com 2020/2/5
 */
@Service
public class UserImportCreateServiceImpl extends UserBuildService implements UserImportCreateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserImportCreateServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserCheckService userCheckService;
    @Autowired
    private UserPasswordService userPasswordService;
    @Autowired
    private MemberRoleAssignService memberRoleAssignService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importCreateUser(User user) {
        {
            LOGGER.info("begin create user, user={}", user);
            User existUser = this.autoRegisterUserWhenExist(user);
            if (existUser == null) {
                this.initUser(user);
                this.checkValidity(user);
                this.checkLoginName(user);
                this.generateLoginName(user);
                this.handleBeforePersist(user);
                this.persistUser(user);
                this.handleUserInfo(user);
                this.handleMemberRole(user);
                this.handleUserConfig(user);
                this.cacheUser(user);
                LOGGER.info("end create user, user={}", user);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected User autoRegisterUserWhenExist(User user) {
        user.setUserType(StringUtils.defaultIfBlank(user.getUserType(), UserType.PLATFORM.value()));
        List<User> users = null;
        if (StringUtils.isNotBlank(user.getPhone())) {
            users = this.userRepository.selectByCondition(Condition.builder(User.class).select(User.FIELD_ID, User.FIELD_LOGIN_NAME, User.FIELD_USER_TYPE, User.FIELD_OBJECT_VERSION_NUMBER).where(Sqls.custom().andEqualTo(User.FIELD_PHONE, user.getPhone())).build());
        }

        if (StringUtils.isNotBlank(user.getEmail())) {
            users = this.userRepository.selectByCondition(Condition.builder(User.class).select(User.FIELD_ID, User.FIELD_LOGIN_NAME, User.FIELD_USER_TYPE, User.FIELD_OBJECT_VERSION_NUMBER).where(Sqls.custom().andEqualTo(User.FIELD_EMAIL, user.getEmail())).build());
        }

        User existsUser = CollectionUtils.isNotEmpty(users) && users.get(0) != null ? (User) users.get(0) : null;
        if (existsUser != null) {
            LOGGER.info("auto register user, user={}, existsUser={}", user, existsUser);
            Set<String> types = Sets.newHashSet(existsUser.getUserType().split(","));
            if (types.contains(user.getUserType())) {
                throw new CommonException("hiam.info.userRegistered");
            } else {
                types.add(user.getUserType());
                existsUser.setUserType(StringUtils.join(types.toArray(), ","));
                userRepository.updateOptional(existsUser, User.FIELD_USER_TYPE);
                UserInfo existsUserInfo = userRepository.selectUserInfoByPrimaryKey(existsUser.getId());
                existsUserInfo.setPhoneCheckFlag(Optional.ofNullable(user.getPhoneCheckFlag()).orElse(existsUserInfo.getPhoneCheckFlag()));
                existsUserInfo.setEmailCheckFlag(Optional.ofNullable(user.getEmailCheckFlag()).orElse(existsUserInfo.getEmailCheckFlag()));
                existsUserInfo.setAddressDetail(Optional.ofNullable(user.getAddressDetail()).orElse(existsUserInfo.getAddressDetail()));
                existsUserInfo.setBirthday(Optional.ofNullable(user.getBirthday()).orElse(existsUserInfo.getBirthday()));
                existsUserInfo.setGender(Optional.ofNullable(user.getGender()).orElse(existsUserInfo.getGender()));
                existsUserInfo.setNickname(Optional.ofNullable(user.getNickname()).orElse(existsUserInfo.getNickname()));
                userRepository.updateUserInfoByPrimaryKey(existsUserInfo);
                user.setId(existsUser.getId());
                user.setLoginName(existsUser.getLoginName());
                user.setRegistered(true);
                user.setRegisteredMessage(MessageAccessor.getMessage("hiam.info.userRegistered").desc());
                this.handleMemberRole(user);
                return user;
            }
        } else {
            return null;
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
            this.checkUserMemberRole(user);
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
        this.userRepository.insertUserInfoSelective(userInfo);
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

    protected void checkUserMemberRole(User user) {
        if (CollectionUtils.isEmpty(user.getMemberRoleList())) {
            throw new CommonException("hiam.warn.user.assignLeastOneRole");
        } else {
            Map<Long, List<TenantRoleDTO>> map = (Optional.ofNullable(user.getDefaultRoles()).orElse(Collections.emptyList())).stream().filter((t) -> {
                return Objects.equals(t.getDefaultFlag(), BaseConstants.Flag.YES);
            }).collect(Collectors.groupingBy(TenantRoleDTO::getTenantId));
            map.forEach((k, v) -> {
                if (v.size() > 1) {
                    throw new IllegalArgumentException("Only one default role is allowed under the same tenant.");
                }
            });
        }
    }
}
