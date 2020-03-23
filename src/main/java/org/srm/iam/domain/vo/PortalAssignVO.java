package org.srm.iam.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 门户分配
 *
 * @author yunxiang.zhou01@hand-china.com 2018-08-13 15:37:15
 */
@ApiModel("门户分配")
@VersionAudit
@ModifyAudit
@Table(name = "spfm_portal_assign")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortalAssignVO extends AuditDomain {

    public static final String FIELD_ASSIGN_ID = "assignId";
    public static final String FIELD_GROUP_ID = "groupId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_WEB_URL = "webUrl";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";

    /**
     * 校验分组专用--租户分配时公司不能为空
     */
    public static interface TenantAssign {}
    /**
     * 校验分组专用--平台分配时公司可以为空
     */
    public static interface PlatformAssign {}



    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long assignId;
    private Long groupId;
    @ApiModelProperty(value = "客户租户ID")
    @NotNull
    private Long tenantId;
    @NotNull(groups = TenantAssign.class)
    private Long companyId;
    private String webUrl;
    @ApiModelProperty(value = "启用标识")
    @NotNull
    private Integer enabledFlag;

    @ApiModelProperty(value = "默认企业间屏蔽，1是，0否")
    private Integer interBusinessShield;
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getAssignId() {
        return assignId;
    }

    public void setAssignId(Long assignId) {
        this.assignId = assignId;
    }

    /**
     * @return 集团ID FND_GROUP.GROUP_ID
     */
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * @return 客户租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 客户公司ID，HPFM_COMPANY.COMPANY_ID
     */
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * @return 域名
     */
    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    /**
     * @return 启用标识
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Integer getInterBusinessShield() {
        return interBusinessShield;
    }

    public void setInterBusinessShield(Integer interBusinessShield) {
        this.interBusinessShield = interBusinessShield;
    }
}
