package org.hzero.iam.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.iam.domain.repository.DomainRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;

@ApiModel("门户分配")
@VersionAudit
@ModifyAudit
@Table(name = "hiam_domain")
public class Domain extends AuditDomain {
    public static final String FIELD_DOMAIN_ID = "domainId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_DOMAIN_URL = "domainUrl";
    public static final String FIELD_SSO_TYPE_CODE = "ssoTypeCode";
    public static final String FIELD_SSO_SERVER_URL = "ssoServerUrl";
    public static final String FIELD_SSO_LOGIN_URL = "ssoLoginUrl";
    public static final String FIELD_SSO_LOGOUT_URL = "ssoLogoutUrl";
    public static final String FIELD_SSO_CLIENT_ID = "ssoClientId";
    public static final String FIELD_SSO_CLIENT_PWD = "ssoClientPwd";
    public static final String FIELD_SSO_USER_INFO = "ssoUserInfo";
    public static final String FIELD_SAML_META_URL = "samlMetaUrl";
    public static final String FIELD_CLIENT_HOST_URL = "clientHostUrl";
    public static final String FIELD_REMARK = "remark";
    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private Long domainId;
    @ApiModelProperty("客户租户ID")
    @NotNull
    private Long tenantId;
    @ApiModelProperty("客户公司ID，HPFM_COMPANY.COMPANY_ID")
    private Long companyId;
    @ApiModelProperty("域名")
    @NotBlank
    private String domainUrl;
    @ApiModelProperty("CAS|CAS2|SAML|IDM|OAUTH2|NULL")
    @LovValue(lovCode = "HIAM.SSO_TYPE_CODE")
    private String ssoTypeCode;
    @ApiModelProperty("CAS3登录名属性")
    private String loginNameField;
    @ApiModelProperty("单点认证服务器地址")
    private String ssoServerUrl;
    @ApiModelProperty("单点登录地址")
    private String ssoLoginUrl;
    @ApiModelProperty("单点登出地址")
    private String ssoLogoutUrl;
    @ApiModelProperty("客户端URL")
    private String clientHostUrl;
    @ApiModelProperty("备注说明")
    private String remark;
    private String ssoClientId;
    private String ssoClientPwd;
    private String ssoUserInfo;
    private String samlMetaUrl;
    @Transient
    private String ssoTypeMeaning;
    @Transient
    private String tenantName;
    @Transient
    private String companyName;

    public Domain() {
    }

    public void vaidateDomainUrl(DomainRepository domainRepository) {
        Integer existFlag = domainRepository.selectCountByCondition(Condition.builder(Domain.class).andWhere(Sqls.custom().andEqualTo("domainUrl", this.domainUrl).andNotEqualTo("domainId", this.domainId, true)).build());
        if (existFlag != 0) {
            throw new CommonException("domain.domain-url.exist", new Object[0]);
        }
    }

    public Long getDomainId() {
        return this.domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getDomainUrl() {
        return this.domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getSsoTypeCode() {
        return this.ssoTypeCode;
    }

    public void setSsoTypeCode(String ssoTypeCode) {
        this.ssoTypeCode = ssoTypeCode;
    }

    public String getSsoServerUrl() {
        return this.ssoServerUrl;
    }

    public void setSsoServerUrl(String ssoServerUrl) {
        this.ssoServerUrl = ssoServerUrl;
    }

    public String getSsoLoginUrl() {
        return this.ssoLoginUrl;
    }

    public void setSsoLoginUrl(String ssoLoginUrl) {
        this.ssoLoginUrl = ssoLoginUrl;
    }

    public String getClientHostUrl() {
        return this.clientHostUrl;
    }

    public void setClientHostUrl(String clientHostUrl) {
        this.clientHostUrl = clientHostUrl;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSsoTypeMeaning() {
        return this.ssoTypeMeaning;
    }

    public void setSsoTypeMeaning(String ssoTypeMeaning) {
        this.ssoTypeMeaning = ssoTypeMeaning;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSsoClientId() {
        return this.ssoClientId;
    }

    public void setSsoClientId(String ssoClientId) {
        this.ssoClientId = ssoClientId;
    }

    public String getSsoClientPwd() {
        return this.ssoClientPwd;
    }

    public void setSsoClientPwd(String ssoClientPwd) {
        this.ssoClientPwd = ssoClientPwd;
    }

    public String getSsoUserInfo() {
        return this.ssoUserInfo;
    }

    public void setSsoUserInfo(String ssoUserInfo) {
        this.ssoUserInfo = ssoUserInfo;
    }

    public String getSsoLogoutUrl() {
        return this.ssoLogoutUrl;
    }

    public void setSsoLogoutUrl(String ssoLogoutUrl) {
        this.ssoLogoutUrl = ssoLogoutUrl;
    }

    public String getSamlMetaUrl() {
        return this.samlMetaUrl;
    }

    public void setSamlMetaUrl(String samlMetaUrl) {
        this.samlMetaUrl = samlMetaUrl;
    }

    public String getLoginNameField() {
        return loginNameField;
    }

    public void setLoginNameField(String loginNameField) {
        this.loginNameField = loginNameField;
    }
}
