//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hzero.iam.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hzero.boot.oauth.util.PasswordUtils;
import org.hzero.iam.api.dto.TenantRoleDTO;
import org.hzero.iam.infra.util.CheckStrength;
import org.hzero.mybatis.common.query.Where;
import org.springframework.format.annotation.DateTimeFormat;

@JsonInclude(Include.NON_NULL)
@ModifyAudit
@VersionAudit
@Table(
        name = "iam_user"
)
@ApiModel("用户")
public class User extends AuditDomain {
    public static final String FIELD_ID = "id";
    public static final String FIELD_LOGIN_NAME = "loginName";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_REAL_NAME = "realName";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_INTERNATIONAL_TEL_CODE = "internationalTelCode";
    public static final String FIELD_IMAGE_URL = "imageUrl";
    public static final String FIELD_PROFILE_PHOTO = "profilePhoto";
    public static final String FIELD_LANGUAGE = "language";
    public static final String FIELD_TIME_ZONE = "timeZone";
    public static final String FIELD_LAST_PASSWORD_UPDATED_AT = "lastPasswordUpdatedAt";
    public static final String FIELD_LAST_LOGIN_AT = "lastLoginAt";
    public static final String FIELD_ENABLED = "enabled";
    public static final String FIELD_LOCKED = "locked";
    public static final String FIELD_LDAP = "ldap";
    public static final String FIELD_LOCKED_UNTIL_AT = "lockedUntilAt";
    public static final String FIELD_PASSWORD_ATTEMPT = "passwordAttempt";
    public static final String FIELD_ADMIN = "admin";
    public static final String FIELD_COMPANY_NAME = "companyName";
    public static final String FIELD_BIRTHDAY = "birthday";
    public static final String FIELD_NICKNAME = "nickname";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_ORGION_ID = "regionId";
    public static final String FIELD_ADDRESS_DETAIL = "addressDetail";
    public static final String FIELD_START_DATE_ACTIVE = "startDateActive";
    public static final String FIELD_END_DATE_ACTIVE = "endDateActive";
    public static final String FIELD_PHONE_CHECK_FLAG = "phoneCheckFlag";
    public static final String FIELD_EMAIL_CHECK_FLAG = "emailCheckFlag";
    public static final String FIELD_DEFAULT_ROLE_ID = "defaultRoleId";
    public static final String FIELD_USER_TYPE = "userType";
    public static final String DEFAULT_LANGUAGE = "zh_CN";
    public static final String DEFAULT_TIME_ZONE = "GMT+8";
    @Id
    @Where
    @GeneratedValue
    private Long id;
    @Length(
            max = 128
    )
    @ApiModelProperty("登录账号，未传则生成默认账号")
    private String loginName;
    @Length(
            max = 128
    )
    @Email
    @ApiModelProperty("邮箱")
    private String email;
    @Where
    @ApiModelProperty(
            value = "所属租户ID",
            required = true
    )
    private Long organizationId;
    @Column(
            name = "hash_password"
    )
    @ApiModelProperty(
            value = "密码",
            required = true
    )
    @Length(
            max = 128
    )
    private String password;
    @ApiModelProperty(
            value = "真实姓名",
            required = true
    )
    @Length(
            max = 128
    )
    private String realName;
    @ApiModelProperty("手机号")
    @Length(
            max = 32
    )
    private String phone;
    @ApiModelProperty("国际冠码，默认+86")
    private String internationalTelCode;
    @ApiModelProperty("用户头像地址")
    @Length(
            max = 480
    )
    private String imageUrl;
    @ApiModelProperty("用户二进制头像")
    private String profilePhoto;
    @ApiModelProperty("语言，默认 zh_CN")
    private String language;
    @ApiModelProperty("时区，默认 GMT+8")
    private String timeZone;
    @ApiModelProperty("密码最后一次修改时间")
    private Date lastPasswordUpdatedAt;
    @ApiModelProperty("最近登录时间")
    private Date lastLoginAt;
    @Column(
            name = "is_enabled"
    )
    @ApiModelProperty("是否启用")
    private Boolean enabled;
    @Transient
    private Integer enabledFlag;
    @Column(
            name = "is_locked"
    )
    @ApiModelProperty("是否锁定")
    private Boolean locked;
    @Transient
    private Integer lockedFlag;
    @Column(
            name = "is_ldap"
    )
    @ApiModelProperty("是否LDAP用户")
    private Boolean ldap;
    @Transient
    private Integer ldapFlag;
    @ApiModelProperty("锁定时间")
    private Date lockedUntilAt;
    @ApiModelProperty("密码尝试次数")
    private Integer passwordAttempt;
    @Column(
            name = "is_admin"
    )
    @ApiModelProperty("是否超级管理员")
    private Boolean admin;
    @Transient
    private Integer adminFlag;
    @ApiModelProperty("用户类型，中台用户-P，C端用户-C")
    private String userType;
    @Transient
    private List<Role> roles;
    @Transient
    @ApiModelProperty("公司名称")
    private String companyName;
    @Transient
    @ApiModelProperty("邀请码")
    private String invitationCode;
    @Transient
    @ApiModelProperty("员工ID")
    private Long employeeId;
    @Transient
    @ApiModelProperty("协议ID")
    private Long textId;
    @Transient
    @ApiModelProperty("密码安全等级")
    private String securityLevelCode;
    @JsonFormat(
            pattern = "yyyy-MM-dd"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    @Transient
    @ApiModelProperty("有效日期起，默认当前时间")
    private LocalDate startDateActive;
    @JsonFormat(
            pattern = "yyyy-MM-dd"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    @Transient
    @ApiModelProperty("有效日期止")
    private LocalDate endDateActive;
    @Transient
    @ApiModelProperty("用户来源")
    private Integer userSource;
    @Transient
    @ApiModelProperty("手机号是否验证通过")
    private Integer phoneCheckFlag;
    @Transient
    @ApiModelProperty("邮箱是否验证通过")
    private Integer emailCheckFlag;
    @Transient
    @ApiModelProperty("密码是否重置过")
    private Integer passwordResetFlag;
    @Transient
    private List<TenantRoleDTO> defaultRoles;
    @Transient
    private Long defaultCompanyId;
    @Transient
    private Date lockedDate;
    @JsonFormat(
            pattern = "yyyy-MM-dd"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    @Transient
    @ApiModelProperty("生日")
    private LocalDate birthday;
    @Transient
    @ApiModelProperty("昵称")
    private String nickname;
    @Transient
    @ApiModelProperty("性别")
    private Integer gender;
    @Transient
    @ApiModelProperty("国家ID")
    private Long countryId;
    @Transient
    @ApiModelProperty("区域ID")
    private Long regionId;
    @Transient
    @ApiModelProperty("地址详细")
    private String addressDetail;
    @Transient
    @ApiModelProperty("角色编码")
    private String roleCode;
    @Transient
    @ApiModelProperty("角色名称")
    private String roleName;
    @Transient
    @ApiModelProperty("授权用户")
    private String includeAllFlag;
    @Transient
    private String anotherPassword;
    @Transient
    @ApiModelProperty("用户角色列表")
    private List<MemberRole> memberRoleList;
    @Transient
    private String tenantName;
    @Transient
    private String tenantNum;
    @Transient
    private Long sourceId;
    @Transient
    private String assignLevel;
    @Transient
    private String assignLevelMeaning;
    @Transient
    private Long assignLevelValue;
    @Transient
    private String assignLevelValueMeaning;
    @Transient
    private boolean registered = false;
    @Transient
    private String registeredMessage;
    @Transient
    @JsonIgnore
    private String tmpPassword;
    @Transient
    private String uuid;
    @Transient
    private Boolean checkPasswordPolicy;

    public void clearPassword() {
        this.password = null;
        this.anotherPassword = null;
        this.tmpPassword = null;
    }

    public void unlocked() {
        this.locked = false;
        this.lockedDate = null;
        this.lockedUntilAt = null;
    }

    public void locked() {
        this.locked = true;
        this.lockedDate = new Date();
    }

    public void unfrozen() {
        this.enabled = true;
    }

    public void frozen() {
        this.enabled = false;
    }

    public void encodePassword() {
        this.securityLevelCode = CheckStrength.getPasswordLevel(this.password).name();
        this.password = PasswordUtils.encodedPassword(this.getPassword());
    }

    public Boolean comparePassword(String originalPassword) {
        return PasswordUtils.checkPasswordMatch(originalPassword, this.password);
    }

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public List<TenantRoleDTO> getDefaultRoles() {
        return this.defaultRoles;
    }

    public void setDefaultRoles(List<TenantRoleDTO> defaultRoles) {
        this.defaultRoles = defaultRoles;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = StringUtils.lowerCase(email);
    }

    public Long getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInternationalTelCode() {
        return this.internationalTelCode;
    }

    public void setInternationalTelCode(String internationalTelCode) {
        this.internationalTelCode = internationalTelCode;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProfilePhoto() {
        return this.profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Date getLastPasswordUpdatedAt() {
        return this.lastPasswordUpdatedAt;
    }

    public void setLastPasswordUpdatedAt(Date lastPasswordUpdatedAt) {
        this.lastPasswordUpdatedAt = lastPasswordUpdatedAt;
    }

    public Date getLastLoginAt() {
        return this.lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getLocked() {
        return this.locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getLdap() {
        return this.ldap;
    }

    public void setLdap(Boolean ldap) {
        this.ldap = ldap;
    }

    public Date getLockedUntilAt() {
        return this.lockedUntilAt;
    }

    public void setLockedUntilAt(Date lockedUntilAt) {
        this.lockedUntilAt = lockedUntilAt;
    }

    public Integer getPasswordAttempt() {
        return this.passwordAttempt;
    }

    public void setPasswordAttempt(Integer passwordAttempt) {
        this.passwordAttempt = passwordAttempt;
    }

    public void lockUtilAt(Date date) {
        this.lockedUntilAt = date;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public Boolean getAdmin() {
        return this.admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInvitationCode() {
        return this.invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Long getTextId() {
        return this.textId;
    }

    public void setTextId(Long textId) {
        this.textId = textId;
    }

    public String getSecurityLevelCode() {
        return this.securityLevelCode;
    }

    public void setSecurityLevelCode(String securityLevelCode) {
        this.securityLevelCode = securityLevelCode;
    }

    public LocalDate getStartDateActive() {
        return this.startDateActive;
    }

    public void setStartDateActive(LocalDate startDateActive) {
        this.startDateActive = startDateActive;
    }

    public LocalDate getEndDateActive() {
        return this.endDateActive;
    }

    public void setEndDateActive(LocalDate endDateActive) {
        this.endDateActive = endDateActive;
    }

    public Integer getUserSource() {
        return this.userSource;
    }

    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    public Integer getPhoneCheckFlag() {
        return this.phoneCheckFlag;
    }

    public void setPhoneCheckFlag(Integer phoneCheckFlag) {
        this.phoneCheckFlag = phoneCheckFlag;
    }

    public Integer getEmailCheckFlag() {
        return this.emailCheckFlag;
    }

    public void setEmailCheckFlag(Integer emailCheckFlag) {
        this.emailCheckFlag = emailCheckFlag;
    }

    public Integer getPasswordResetFlag() {
        return this.passwordResetFlag;
    }

    public void setPasswordResetFlag(Integer passwordResetFlag) {
        this.passwordResetFlag = passwordResetFlag;
    }

    public Long getDefaultCompanyId() {
        return this.defaultCompanyId;
    }

    public void setDefaultCompanyId(Long defaultCompanyId) {
        this.defaultCompanyId = defaultCompanyId;
    }

    public Date getLockedDate() {
        return this.lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    public String getAnotherPassword() {
        return this.anotherPassword;
    }

    public void setAnotherPassword(String anotherPassword) {
        this.anotherPassword = anotherPassword;
    }

    public List<MemberRole> getMemberRoleList() {
        return this.memberRoleList;
    }

    public void setMemberRoleList(List<MemberRole> memberRoleList) {
        this.memberRoleList = memberRoleList;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantNum() {
        return this.tenantNum;
    }

    public void setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
    }

    public Long getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getAssignLevel() {
        return this.assignLevel;
    }

    public void setAssignLevel(String assignLevel) {
        this.assignLevel = assignLevel;
    }

    public String getAssignLevelMeaning() {
        return this.assignLevelMeaning;
    }

    public void setAssignLevelMeaning(String assignLevelMeaning) {
        this.assignLevelMeaning = assignLevelMeaning;
    }

    public Long getAssignLevelValue() {
        return this.assignLevelValue;
    }

    public void setAssignLevelValue(Long assignLevelValue) {
        this.assignLevelValue = assignLevelValue;
    }

    public String getAssignLevelValueMeaning() {
        return this.assignLevelValueMeaning;
    }

    public void setAssignLevelValueMeaning(String assignLevelValueMeaning) {
        this.assignLevelValueMeaning = assignLevelValueMeaning;
    }

    public Integer getEnabledFlag() {
        return this.enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Integer getLockedFlag() {
        return this.lockedFlag;
    }

    public void setLockedFlag(Integer lockedFlag) {
        this.lockedFlag = lockedFlag;
    }

    public Integer getLdapFlag() {
        return this.ldapFlag;
    }

    public void setLdapFlag(Integer ldapFlag) {
        this.ldapFlag = ldapFlag;
    }

    public Integer getAdminFlag() {
        return this.adminFlag;
    }

    public void setAdminFlag(Integer adminFlag) {
        this.adminFlag = adminFlag;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIncludeAllFlag() {
        return this.includeAllFlag;
    }

    public void setIncludeAllFlag(String includeAllFlag) {
        this.includeAllFlag = includeAllFlag;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return this.gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getRegionId() {
        return this.regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getAddressDetail() {
        return this.addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public boolean isRegistered() {
        return this.registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getRegisteredMessage() {
        return this.registeredMessage;
    }

    public void setRegisteredMessage(String registeredMessage) {
        this.registeredMessage = registeredMessage;
    }

    public String getTmpPassword() {
        return this.tmpPassword;
    }

    public void setTmpPassword(String tmpPassword) {
        this.tmpPassword = tmpPassword;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getCheckPasswordPolicy() {
        return this.checkPasswordPolicy;
    }

    public void setCheckPasswordPolicy(Boolean checkPasswordPolicy) {
        this.checkPasswordPolicy = checkPasswordPolicy;
    }

    public String toString() {
        return (new StringJoiner(", ", User.class.getSimpleName() + "[", "]")).add("id=" + this.id).add("loginName='" + this.loginName + "'").add("email='" + this.email + "'").add("organizationId=" + this.organizationId).add("realName='" + this.realName + "'").add("phone='" + this.phone + "'").add("internationalTelCode='" + this.internationalTelCode + "'").add("language='" + this.language + "'").add("timeZone='" + this.timeZone + "'").add("enabled=" + this.enabled).add("locked=" + this.locked).add("ldap=" + this.ldap).add("admin=" + this.admin).add("userType='" + this.userType + "'").add("companyName='" + this.companyName + "'").add("invitationCode='" + this.invitationCode + "'").add("employeeId=" + this.employeeId).add("textId=" + this.textId).add("startDateActive=" + this.startDateActive).add("endDateActive=" + this.endDateActive).add("userSource=" + this.userSource).add("phoneCheckFlag=" + this.phoneCheckFlag).add("emailCheckFlag=" + this.emailCheckFlag).add("passwordResetFlag=" + this.passwordResetFlag).add("lockedDate=" + this.lockedDate).add("birthday=" + this.birthday).add("nickname='" + this.nickname + "'").add("gender=" + this.gender).add("countryId=" + this.countryId).add("regionId=" + this.regionId).add("addressDetail='" + this.addressDetail + "'").add("memberRoleList=" + this.memberRoleList).toString();
    }
}
