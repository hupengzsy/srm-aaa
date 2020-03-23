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
 * @author zhiwei.zhou@hand-china.com 2019-07-23 13:45:18
 */
@ApiModel("菜单")
@VersionAudit
@ModifyAudit
@Table(name = "iam_menu")
public class Menu extends AuditDomain {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_H_QUICK_INDEX = "hQuickIndex";
    public static final String FIELD_FD_LEVEL = "fdLevel";
    public static final String FIELD_PARENT_ID = "parentId";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_SORT = "sort";
    public static final String FIELD_IS_DEFAULT = "isDefault";
    public static final String FIELD_ICON = "icon";
    public static final String FIELD_ROUTE = "route";
    public static final String FIELD_H_CUSTOM_FLAG = "hCustomFlag";
    public static final String FIELD_H_TENANT_ID = "hTenantId";
    public static final String FIELD_H_LEVEL_PATH = "hLevelPath";
    public static final String FIELD_H_VIRTUAL_FLAG = "hVirtualFlag";
    public static final String FIELD_H_CONTROLLER_TYPE = "hControllerType";
    public static final String FIELD_H_DESCRIPTION = "hDescription";
    public static final String FIELD_H_ENABLED_FLAG = "hEnabledFlag";
    public static final String FIELD_CATEGORY = "category";

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
    @ApiModelProperty(value = "菜单的标识", required = true)
    @NotBlank
    private String code;
    @ApiModelProperty(value = "菜单名", required = true)
    @NotBlank
    private String name;
    @ApiModelProperty(value = "款速索引")
    private String hQuickIndex;
    @ApiModelProperty(value = "菜单层级", required = true)
    @NotBlank
    private String fdLevel;
    @ApiModelProperty(value = "父级菜单id", required = true)
    @NotNull
    private Long parentId;
    @ApiModelProperty(value = "菜单类型， 包括三种（root, dir, menu）", required = true)
    @NotBlank
    private String type;
    @ApiModelProperty(value = "菜单顺序")
    private Long sort;
    @ApiModelProperty(value = "是否是默认菜单,0不是默认菜单，1是默认菜单", required = true)
    @NotNull
    private Integer isDefault;
    @ApiModelProperty(value = "图标的code值")
    private String icon;
    @ApiModelProperty(value = "路由")
    private String route;
    @ApiModelProperty(value = "客户化菜单标识", required = true)
    @NotNull
    private Integer hCustomFlag;
    @ApiModelProperty(value = "客户化菜单租户标识", required = true)
    @NotNull
    private Long hTenantId;
    @ApiModelProperty(value = "层级路径, RootId/../ParentId/Id")
    private String hLevelPath;
    @ApiModelProperty(value = "是否虚拟菜单, 虚拟菜单不参与左侧菜单栏展示")
    private Integer hVirtualFlag;
    @ApiModelProperty(value = "控制类型")
    private String hControllerType;
    @ApiModelProperty(value = "描述")
    private String hDescription;
    @ApiModelProperty(value = "是否启用", required = true)
    @NotNull
    private Integer hEnabledFlag;
    @ApiModelProperty(value = "项目层菜单分类，可以为AGILE，PROGRAM，ANALYTICAL")
    private String category;

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
     * @return 菜单的标识
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return 菜单名
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 款速索引
     */
    public String getHQuickIndex() {
        return hQuickIndex;
    }

    public void setHQuickIndex(String hQuickIndex) {
        this.hQuickIndex = hQuickIndex;
    }

    /**
     * @return 菜单层级
     */
    public String getFdLevel() {
        return fdLevel;
    }

    public void setFdLevel(String fdLevel) {
        this.fdLevel = fdLevel;
    }

    /**
     * @return 父级菜单id
     */
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return 菜单类型， 包括三种（root, dir, menu）
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return 菜单顺序
     */
    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    /**
     * @return 是否是默认菜单,0不是默认菜单，1是默认菜单
     */
    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * @return 图标的code值
     */
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return 路由
     */
    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    /**
     * @return 客户化菜单标识
     */
    public Integer getHCustomFlag() {
        return hCustomFlag;
    }

    public void setHCustomFlag(Integer hCustomFlag) {
        this.hCustomFlag = hCustomFlag;
    }

    /**
     * @return 客户化菜单租户标识
     */
    public Long getHTenantId() {
        return hTenantId;
    }

    public void setHTenantId(Long hTenantId) {
        this.hTenantId = hTenantId;
    }

    /**
     * @return 层级路径, RootId/../ParentId/Id
     */
    public String getHLevelPath() {
        return hLevelPath;
    }

    public void setHLevelPath(String hLevelPath) {
        this.hLevelPath = hLevelPath;
    }

    /**
     * @return 是否虚拟菜单, 虚拟菜单不参与左侧菜单栏展示
     */
    public Integer getHVirtualFlag() {
        return hVirtualFlag;
    }

    public void setHVirtualFlag(Integer hVirtualFlag) {
        this.hVirtualFlag = hVirtualFlag;
    }

    /**
     * @return 控制类型
     */
    public String getHControllerType() {
        return hControllerType;
    }

    public void setHControllerType(String hControllerType) {
        this.hControllerType = hControllerType;
    }

    /**
     * @return
     */
    public String getHDescription() {
        return hDescription;
    }

    public void setHDescription(String hDescription) {
        this.hDescription = hDescription;
    }

    /**
     * @return
     */
    public Integer getHEnabledFlag() {
        return hEnabledFlag;
    }

    public void setHEnabledFlag(Integer hEnabledFlag) {
        this.hEnabledFlag = hEnabledFlag;
    }

    /**
     * @return 项目层菜单分类，可以为AGILE，PROGRAM，ANALYTICAL
     */
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
