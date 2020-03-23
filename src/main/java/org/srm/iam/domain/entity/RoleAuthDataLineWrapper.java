package org.srm.iam.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel("角色单据权限管理行")
@VersionAudit
@ModifyAudit
@Table(
        name = "hiam_role_auth_data_line"
)
@JsonInclude(Include.NON_NULL)
public class RoleAuthDataLineWrapper extends AuditDomain {
    public static final String FIELD_AUTH_DATA_LINE_ID = "authDataLineId";
    public static final String FIELD_AUTH_DATA_ID = "authDataId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DATA_ID = "dataId";
    public static final String FIELD_DATA_CODE = "dataCode";
    public static final String FIELD_DATA_NAME = "dataName";
    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long authDataLineId;
    @ApiModelProperty(
            value = "权限ID，hiam_role_auth_data.auth_data_id",
            required = true
    )
    @NotNull
    private Long authDataId;
    @ApiModelProperty(
            value = "租户ID，HPFM.HPFM_TENANT",
            required = true
    )
    @NotNull
    private Long tenantId;
    @ApiModelProperty(
            value = "数据ID",
            required = true
    )
    @NotNull
    private Long dataId;
    @ApiModelProperty("数据代码/编码")
    @Length(
            max = 80
    )
    private String dataCode;
    @ApiModelProperty("数据名称")
    @Length(
            max = 360
    )
    private String dataName;
    @Transient
    private String tenantName;

    public RoleAuthDataLineWrapper() {
    }

    public Long getAuthDataLineId() {
        return this.authDataLineId;
    }

    public RoleAuthDataLineWrapper setAuthDataLineId(Long authDataLineId) {
        this.authDataLineId = authDataLineId;
        return this;
    }

    public Long getAuthDataId() {
        return this.authDataId;
    }

    public RoleAuthDataLineWrapper setAuthDataId(Long authDataId) {
        this.authDataId = authDataId;
        return this;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public RoleAuthDataLineWrapper setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public Long getDataId() {
        return this.dataId;
    }

    public RoleAuthDataLineWrapper setDataId(Long dataId) {
        this.dataId = dataId;
        return this;
    }

    public String getDataCode() {
        return this.dataCode;
    }

    public RoleAuthDataLineWrapper setDataCode(String dataCode) {
        this.dataCode = dataCode;
        return this;
    }

    public String getDataName() {
        return this.dataName;
    }

    public RoleAuthDataLineWrapper setDataName(String dataName) {
        this.dataName = dataName;
        return this;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public RoleAuthDataLineWrapper setTenantName(String tenantName) {
        this.tenantName = tenantName;
        return this;
    }
}
