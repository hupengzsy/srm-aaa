package org.srm.iam.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 *
 * @author zhiwei.zhou@hand-china.com 2019-07-19 14:34:30
 */
@ApiModel("角色")
@VersionAudit
@ModifyAudit
@Table(name = "iam_role")
public class Role extends AuditDomain {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_FD_LEVEL = "fdLevel";
    public static final String FIELD_H_TENANT_ID = "hTenantId";
    public static final String FIELD_H_INHERIT_ROLE_ID = "hInheritRoleId";
    public static final String FIELD_H_PARENT_ROLE_ID = "hParentRoleId";
    public static final String FIELD_H_PARENT_ROLE_ASSIGN_LEVEL = "hParentRoleAssignLevel";
    public static final String FIELD_H_PARENT_ROLE_ASSIGN_LEVEL_VAL = "hParentRoleAssignLevelVal";
    public static final String FIELD_IS_ENABLED = "isEnabled";
    public static final String FIELD_IS_MODIFIED = "isModified";
    public static final String FIELD_IS_ENABLE_FORBIDDEN = "isEnableForbidden";
    public static final String FIELD_IS_BUILT_IN = "isBuiltIn";
    public static final String FIELD_IS_ASSIGNABLE = "isAssignable";
    public static final String FIELD_H_LEVEL_PATH = "hLevelPath";
    public static final String FIELD_H_INHERIT_LEVEL_PATH = "hInheritLevelPath";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "id")
    @Id
    @GeneratedValue
    private Long id;
    @ApiModelProperty(value = "角色名", required = true)
    @NotBlank
    private String name;
    @ApiModelProperty(value = "角色编码", required = true)
    @NotBlank
    private String code;
    @ApiModelProperty(value = "角色描述full description")
    private String description;
    @ApiModelProperty(value = "角色级别", required = true)
    @NotBlank
    private String fdLevel;
    @ApiModelProperty(value = "所属租户ID")
    private Long hTenantId;
    @ApiModelProperty(value = "继承角色ID")
    private Long hInheritRoleId;
    @ApiModelProperty(value = "父级角色ID")
    private Long hParentRoleId;
    @ApiModelProperty(value = "父级角色分配层级")
    private String hParentRoleAssignLevel;
    @ApiModelProperty(value = "父级角色分配值")
    private Long hParentRoleAssignLevelVal;
    @ApiModelProperty(value = "是否启用。1启用，0未启用", required = true)
    @NotNull
    private Integer isEnabled;
    @ApiModelProperty(value = "是否可以修改。1表示可以，0不可以", required = true)
    @NotNull
    private Integer isModified;
    @ApiModelProperty(value = "是否可以被禁用", required = true)
    @NotNull
    private Integer isEnableForbidden;
    @ApiModelProperty(value = "是否内置。1表示是，0表示不是", required = true)
    @NotNull
    private Integer isBuiltIn;
    @ApiModelProperty(value = "是否禁止在更高的层次上分配，禁止project role在organization上分配。1表示可以，0表示不可以", required = true)
    @NotNull
    private Integer isAssignable;
    @ApiModelProperty(value = "")
    private String hLevelPath;
    @ApiModelProperty(value = "角色继承生成层次关系")
    private String hInheritLevelPath;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return 角色名
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 角色编码
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return 角色描述full description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 角色级别
     */
    public String getFdLevel() {
        return fdLevel;
    }

    public void setFdLevel(String fdLevel) {
        this.fdLevel = fdLevel;
    }

    /**
     * @return 所属租户ID
     */
    public Long getHTenantId() {
        return hTenantId;
    }

    public void setHTenantId(Long hTenantId) {
        this.hTenantId = hTenantId;
    }

    /**
     * @return 继承角色ID
     */
    public Long getHInheritRoleId() {
        return hInheritRoleId;
    }

    public void setHInheritRoleId(Long hInheritRoleId) {
        this.hInheritRoleId = hInheritRoleId;
    }

    /**
     * @return 父级角色ID
     */
    public Long getHParentRoleId() {
        return hParentRoleId;
    }

    public void setHParentRoleId(Long hParentRoleId) {
        this.hParentRoleId = hParentRoleId;
    }

    /**
     * @return 父级角色分配层级
     */
    public String getHParentRoleAssignLevel() {
        return hParentRoleAssignLevel;
    }

    public void setHParentRoleAssignLevel(String hParentRoleAssignLevel) {
        this.hParentRoleAssignLevel = hParentRoleAssignLevel;
    }

    /**
     * @return 父级角色分配值
     */
    public Long getHParentRoleAssignLevelVal() {
        return hParentRoleAssignLevelVal;
    }

    public void setHParentRoleAssignLevelVal(Long hParentRoleAssignLevelVal) {
        this.hParentRoleAssignLevelVal = hParentRoleAssignLevelVal;
    }

    /**
     * @return 是否启用。1启用，0未启用
     */
    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * @return 是否可以修改。1表示可以，0不可以
     */
    public Integer getIsModified() {
        return isModified;
    }

    public void setIsModified(Integer isModified) {
        this.isModified = isModified;
    }

    /**
     * @return 是否可以被禁用
     */
    public Integer getIsEnableForbidden() {
        return isEnableForbidden;
    }

    public void setIsEnableForbidden(Integer isEnableForbidden) {
        this.isEnableForbidden = isEnableForbidden;
    }

    /**
     * @return 是否内置。1表示是，0表示不是
     */
    public Integer getIsBuiltIn() {
        return isBuiltIn;
    }

    public void setIsBuiltIn(Integer isBuiltIn) {
        this.isBuiltIn = isBuiltIn;
    }

    /**
     * @return 是否禁止在更高的层次上分配，禁止project role在organization上分配。1表示可以，0表示不可以
     */
    public Integer getIsAssignable() {
        return isAssignable;
    }

    public void setIsAssignable(Integer isAssignable) {
        this.isAssignable = isAssignable;
    }

    /**
     * @return
     */
    public String getHLevelPath() {
        return hLevelPath;
    }

    public void setHLevelPath(String hLevelPath) {
        this.hLevelPath = hLevelPath;
    }

    /**
     * @return 角色继承生成层次关系
     */
    public String getHInheritLevelPath() {
        return hInheritLevelPath;
    }

    public void setHInheritLevelPath(String hInheritLevelPath) {
        this.hInheritLevelPath = hInheritLevelPath;
    }

}
