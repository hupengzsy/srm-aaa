//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hzero.iam.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.cache.CacheValue;
import org.hzero.core.cache.Cacheable;
import org.hzero.core.cache.CacheValue.DataStructure;
import org.hzero.core.message.MessageAccessor;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.infra.constant.HiamResourceLevel;
import org.hzero.mybatis.domian.SecurityToken;

@JsonInclude(Include.NON_NULL)
public class RoleVO implements SecurityToken, Cacheable {
    public static final Map<String, String> ROLE_NAME = new HashMap();
    private Long id;
    private String name;
    private String code;
    private String description;
    @LovValue("HIAM.RESOURCE_LEVEL")
    private String level;
    private Boolean isEnabled;
    private Boolean isModified;
    private Boolean isEnableForbidden;
    private Boolean isBuiltIn;
    private Boolean isAssignable;
    private Long tenantId;
    private Long inheritRoleId;
    private Long parentRoleId;
    private String parentRoleAssignLevel;
    private Long parentRoleAssignLevelValue;
    private Long createdBy;
    private Date creationDate;
    private Long objectVersionNumber;
    private String viewCode;
    private String levelMeaning;
    private String inheritedRoleName;
    private String parentRoleName;
    private String tenantName;
    @LovValue("HIAM.ROLE_SOURCE")
    private String roleSource;
    private Long defaultRoleId;
    private Boolean isDefaultRole;
    @CacheValue(
            key = "hiam:user",
            primaryKey = "createdBy",
            searchKey = "realName",
            structure = DataStructure.MAP_OBJECT
    )
    private String createdUserName;
    private Long memberId;
    private String memberType;
    private Long sourceId;
    private String sourceType;
    @LovValue("HIAM.RESOURCE_LEVEL")
    private String assignLevel;
    private String assignLevelMeaning;
    private Long assignLevelValue;
    private String assignLevelValueMeaning;
    private String roleSourceMeaning;
    private List<PermissionVO> permissions;
    private RoleVO inheritedRole;
    private Integer queryRootNodeFlag;
    private Integer adminFlag = 0;
    private Integer assignedFlag = 0;
    private Integer haveAdminFlag = 0;
    private String levelPath;
    private Long adminRoleId;
    private String adminRoleCode;
    private String adminRoleName;
    private String adminRoleLevel;
    private Long adminRoleTenantId;
    private String adminRoleTenantNum;
    private String adminRoleTenantName;
    private Long parentRoleAssignUnitId;
    private String parentRoleAssignUnitName;
    private Integer manageableFlag = 0;
    @JsonIgnore
    private Long userId;
    private List<Long> excludeRoleIds;
    private List<Long> excludeUserIds;
    private Long excludeUserId;
    private boolean selectAssignedRoleFlag = false;
    private Integer childrenNum;
    private Long memberRoleId;
    private String _token;
    private String parentManageParamStr;

    public static String obtainRoleName(String level, String defaultName, String lang) {
        return MessageAccessor.getMessage((String)ROLE_NAME.get(level), defaultName, LocaleUtils.toLocale(lang)).desc();
    }

    public RoleVO() {
    }

    public RoleVO(Long id) {
        this.id = id;
    }

    public String getViewCode() {
        if (StringUtils.isNotBlank(this.code)) {
            String[] codeArr = StringUtils.split(this.code, "/");
            this.viewCode = codeArr[codeArr.length - 1];
        }

        return this.viewCode;
    }

    public Long getId() {
        return this.id;
    }

    public RoleVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Boolean getEnabled() {
        return this.isEnabled != null ? this.isEnabled : false;
    }
    public boolean enableIsNull(){
        return this.isEnabled==null;
    }

    public void setEnabled(Boolean enabled) {
        this.isEnabled = enabled;
    }

    public Boolean getModified() {
        return this.isModified;
    }

    public void setModified(Boolean modified) {
        this.isModified = modified;
    }

    public Boolean getEnableForbidden() {
        return this.isEnableForbidden;
    }

    public void setEnableForbidden(Boolean enableForbidden) {
        this.isEnableForbidden = enableForbidden;
    }

    public Boolean getBuiltIn() {
        return this.isBuiltIn;
    }

    public void setBuiltIn(Boolean builtIn) {
        this.isBuiltIn = builtIn;
    }

    public Boolean getAssignable() {
        return this.isAssignable;
    }

    public void setAssignable(Boolean assignable) {
        this.isAssignable = assignable;
    }

    public Long getObjectVersionNumber() {
        return this.objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getInheritRoleId() {
        return this.inheritRoleId;
    }

    public void setInheritRoleId(Long inheritRoleId) {
        this.inheritRoleId = inheritRoleId;
    }

    public Long getParentRoleId() {
        return this.parentRoleId;
    }

    public RoleVO setParentRoleId(Long parentRoleId) {
        this.parentRoleId = parentRoleId;
        return this;
    }

    public String getParentRoleAssignLevel() {
        return this.parentRoleAssignLevel;
    }

    public RoleVO setParentRoleAssignLevel(String parentRoleAssignLevel) {
        this.parentRoleAssignLevel = parentRoleAssignLevel;
        return this;
    }

    public Long getParentRoleAssignLevelValue() {
        return this.parentRoleAssignLevelValue;
    }

    public RoleVO setParentRoleAssignLevelValue(Long parentRoleAssignLevelValue) {
        this.parentRoleAssignLevelValue = parentRoleAssignLevelValue;
        return this;
    }

    public String getAssignLevelMeaning() {
        return this.assignLevelMeaning;
    }

    public void setAssignLevelMeaning(String assignLevelMeaning) {
        this.assignLevelMeaning = assignLevelMeaning;
    }

    public String getAssignLevelValueMeaning() {
        return this.assignLevelValueMeaning;
    }

    public void setAssignLevelValueMeaning(String assignLevelValueMeaning) {
        this.assignLevelValueMeaning = assignLevelValueMeaning;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public String getRoleSource() {
        return this.roleSource;
    }

    public void setRoleSource(String roleSource) {
        this.roleSource = roleSource;
    }

    public String getLevelMeaning() {
        return this.levelMeaning;
    }

    public void setLevelMeaning(String levelMeaning) {
        this.levelMeaning = levelMeaning;
    }

    public String getInheritedRoleName() {
        return this.inheritedRoleName;
    }

    public void setInheritedRoleName(String inheritedRoleName) {
        this.inheritedRoleName = inheritedRoleName;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public RoleVO setMemberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public String getMemberType() {
        return this.memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Long getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getAssignLevel() {
        return this.assignLevel;
    }

    public void setAssignLevel(String assignLevel) {
        this.assignLevel = assignLevel;
    }

    public Long getAssignLevelValue() {
        return this.assignLevelValue;
    }

    public void setAssignLevelValue(Long assignLevelValue) {
        this.assignLevelValue = assignLevelValue;
    }

    public List<PermissionVO> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<PermissionVO> permissions) {
        this.permissions = permissions;
    }

    public RoleVO getInheritedRole() {
        return this.inheritedRole;
    }

    public void setInheritedRole(RoleVO inheritedRole) {
        this.inheritedRole = inheritedRole;
    }

    public String getParentRoleName() {
        return this.parentRoleName;
    }

    public void setParentRoleName(String parentRoleName) {
        this.parentRoleName = parentRoleName;
    }

    public List<Long> getExcludeRoleIds() {
        return this.excludeRoleIds;
    }

    public void setExcludeRoleIds(List<Long> excludeRoleIds) {
        this.excludeRoleIds = excludeRoleIds;
    }

    public List<Long> getExcludeUserIds() {
        return this.excludeUserIds;
    }

    public void setExcludeUserIds(List<Long> excludeUserIds) {
        this.excludeUserIds = excludeUserIds;
    }

    public Long getExcludeUserId() {
        return this.excludeUserId;
    }

    public void setExcludeUserId(Long excludeUserId) {
        this.excludeUserId = excludeUserId;
    }

    public Long getMemberRoleId() {
        return this.memberRoleId;
    }

    public void setMemberRoleId(Long memberRoleId) {
        this.memberRoleId = memberRoleId;
    }

    public Long getDefaultRoleId() {
        return this.defaultRoleId;
    }

    public void setDefaultRoleId(Long defaultRoleId) {
        this.defaultRoleId = defaultRoleId;
    }

    public Boolean getDefaultRole() {
        return this.isDefaultRole;
    }

    public void setDefaultRole(Boolean defaultRole) {
        this.isDefaultRole = defaultRole;
    }

    public Integer getAdminFlag() {
        return this.adminFlag;
    }

    public void setAdminFlag(Integer adminFlag) {
        this.adminFlag = adminFlag;
    }

    public Integer getAssignedFlag() {
        return this.assignedFlag;
    }

    public void setAssignedFlag(Integer assignedFlag) {
        this.assignedFlag = assignedFlag;
    }

    public Integer getHaveAdminFlag() {
        return this.haveAdminFlag;
    }

    public void setHaveAdminFlag(Integer haveAdminFlag) {
        this.haveAdminFlag = haveAdminFlag;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRoleSourceMeaning() {
        return this.roleSourceMeaning;
    }

    public void setRoleSourceMeaning(String roleSourceMeaning) {
        this.roleSourceMeaning = roleSourceMeaning;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedUserName() {
        return this.createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public Long getAdminRoleId() {
        return this.adminRoleId;
    }

    public void setAdminRoleId(Long adminRoleId) {
        this.adminRoleId = adminRoleId;
    }

    public String getAdminRoleCode() {
        return this.adminRoleCode;
    }

    public void setAdminRoleCode(String adminRoleCode) {
        this.adminRoleCode = adminRoleCode;
    }

    public String getAdminRoleName() {
        return this.adminRoleName;
    }

    public void setAdminRoleName(String adminRoleName) {
        this.adminRoleName = adminRoleName;
    }

    public String getAdminRoleLevel() {
        return this.adminRoleLevel;
    }

    public void setAdminRoleLevel(String adminRoleLevel) {
        this.adminRoleLevel = adminRoleLevel;
    }

    public Long getAdminRoleTenantId() {
        return this.adminRoleTenantId;
    }

    public void setAdminRoleTenantId(Long adminRoleTenantId) {
        this.adminRoleTenantId = adminRoleTenantId;
    }

    public String getAdminRoleTenantNum() {
        return this.adminRoleTenantNum;
    }

    public void setAdminRoleTenantNum(String adminRoleTenantNum) {
        this.adminRoleTenantNum = adminRoleTenantNum;
    }

    public String getAdminRoleTenantName() {
        return this.adminRoleTenantName;
    }

    public void setAdminRoleTenantName(String adminRoleTenantName) {
        this.adminRoleTenantName = adminRoleTenantName;
    }

    public Long getParentRoleAssignUnitId() {
        return this.parentRoleAssignUnitId;
    }

    public void setParentRoleAssignUnitId(Long parentRoleAssignUnitId) {
        this.parentRoleAssignUnitId = parentRoleAssignUnitId;
    }

    public String getParentRoleAssignUnitName() {
        return this.parentRoleAssignUnitName;
    }

    public void setParentRoleAssignUnitName(String parentRoleAssignUnitName) {
        this.parentRoleAssignUnitName = parentRoleAssignUnitName;
    }

    public boolean isSelectAssignedRoleFlag() {
        return this.selectAssignedRoleFlag;
    }

    public void setSelectAssignedRoleFlag(boolean selectAssignedRoleFlag) {
        this.selectAssignedRoleFlag = selectAssignedRoleFlag;
    }

    public Integer getManageableFlag() {
        return this.manageableFlag;
    }

    public void setManageableFlag(Integer manageableFlag) {
        this.manageableFlag = manageableFlag;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public String get_token() {
        return this._token;
    }

    public Class<? extends SecurityToken> associateEntityClass() {
        return Role.class;
    }

    public Integer getQueryRootNodeFlag() {
        return this.queryRootNodeFlag;
    }

    public void setQueryRootNodeFlag(Integer queryRootNodeFlag) {
        this.queryRootNodeFlag = queryRootNodeFlag;
    }

    public String getLevelPath() {
        return this.levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    public Integer getChildrenNum() {
        return this.childrenNum;
    }

    public void setChildrenNum(Integer childrenNum) {
        this.childrenNum = childrenNum;
    }

    static {
        ROLE_NAME.put(HiamResourceLevel.SITE.value(), "hiam.info.role.siteName");
        ROLE_NAME.put(HiamResourceLevel.ORGANIZATION.value(), "hiam.info.role.tenantName");
    }

    public String getParentManageParamStr() {
        return parentManageParamStr;
    }

    public void setParentManageParamStr(String parentManageParamStr) {
        this.parentManageParamStr = parentManageParamStr;
    }
}
