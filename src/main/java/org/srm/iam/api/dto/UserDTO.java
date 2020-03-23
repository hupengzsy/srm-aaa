package org.srm.iam.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.iam.domain.entity.User;

/**
 * 为了增加数据同步字段而创建的dto
 *
 * @author peng.yang03@hand-china.com 2019/10/31 14:23
 */
public class UserDTO extends User {

    @ApiModelProperty(
            value = "外部系统用户id",
            required = true
    )
    private String esUserId;
    @ApiModelProperty(
            value = "外部系统来源标识",
            required = true
    )
    private String esCode;
    @ApiModelProperty(
            value = "同步批次号",
            required = true
    )
    private Long dataVersion;

    private Long tenantId;

    private String esTenantId;

    public String getEsUserId() {
        return esUserId;
    }

    public void setEsUserId(String esUserId) {
        this.esUserId = esUserId;
    }

    public String getEsCode() {
        return esCode;
    }

    public void setEsCode(String esCode) {
        this.esCode = esCode;
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Long dataVersion) {
        this.dataVersion = dataVersion;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getEsTenantId() {
        return esTenantId;
    }

    public void setEsTenantId(String esTenantId) {
        this.esTenantId = esTenantId;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "esUserId='" + esUserId + '\'' +
                ", esCode='" + esCode + '\'' +
                ", dataVersion=" + dataVersion +
                ", tenantId=" + tenantId +
                ", esTenantId='" + esTenantId + '\'' +
                '}';
    }
}
