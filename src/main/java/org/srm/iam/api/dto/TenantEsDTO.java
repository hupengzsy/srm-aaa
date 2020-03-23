package org.srm.iam.api.dto;

/**
 * 租户外部系统对应dto
 *
 * @author peng.yang03@hand-china.com 2019/10/31 10:58
 */
public class TenantEsDTO {

    private Long tenantEsId;
    private String externalSystemCode;
    private Long tenantId;
    private String esTenantId;
    private Long dataVersion;

    public Long getTenantEsId() {
        return tenantEsId;
    }

    public TenantEsDTO() {}

    public TenantEsDTO(String externalSystemCode, String esTenantId) {
        this.externalSystemCode = externalSystemCode;
        this.esTenantId = esTenantId;
    }

    public void setTenantEsId(Long tenantEsId) {
        this.tenantEsId = tenantEsId;
    }

    public String getExternalSystemCode() {
        return externalSystemCode;
    }

    public void setExternalSystemCode(String externalSystemCode) {
        this.externalSystemCode = externalSystemCode;
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

    public Long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Long dataVersion) {
        this.dataVersion = dataVersion;
    }
}
