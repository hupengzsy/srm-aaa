//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.srm.iam.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@VersionAudit
@ModifyAudit
@Table(
        name = "hiam_user_authority_line"
)
public class SrmUserAuthorityLine extends AuditDomain {
    public static final String FIELD_AUTHORITY_LINE_ID = "authorityLineId";
    public static final String FIELD_AUTHORITY_ID = "authorityId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DATA_ID = "dataId";
    public static final String FIELD_DATA_CODE = "dataCode";
    public static final String FIELD_DATA_NAME = "dataName";
    @Id
    @GeneratedValue
    private Long authorityLineId;
    @NotNull
    private Long authorityId;
    @NotNull
    private Long tenantId;
    @NotNull
    private Long dataId;
    private String dataCode;
    private String dataName;

    public SrmUserAuthorityLine() {
    }

    public SrmUserAuthorityLine(Long authorityId, Long tenantId, Long dataId, String dataCode, String dataName) {
        this.authorityId = authorityId;
        this.tenantId = tenantId;
        this.dataId = dataId;
        this.dataCode = dataCode;
        this.dataName = dataName;
    }

    public Long getAuthorityLineId() {
        return this.authorityLineId;
    }

    public void setAuthorityLineId(Long authorityLineId) {
        this.authorityLineId = authorityLineId;
    }

    public Long getAuthorityId() {
        return this.authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDataId() {
        return this.dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getDataCode() {
        return this.dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataName() {
        return this.dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    @Override
    @JsonIgnore
    public Date getCreationDate() {
        return super.getCreationDate();
    }

    @Override
    @JsonIgnore
    public Long getCreatedBy() {
        return super.getCreatedBy();
    }

    @Override
    @JsonIgnore
    public Date getLastUpdateDate() {
        return super.getLastUpdateDate();
    }

    @Override
    @JsonIgnore
    public Long getLastUpdatedBy() {
        return super.getLastUpdatedBy();
    }
}
