package org.hzero.iam.api.dto;

import java.io.Serializable;

/**
 * @author : peng.yu01@hand-china.com 2019/12/9 20:00
 */
public class DomainDTO implements Serializable {
    private static final long serialVersionUID = -6625864894934365970L;
    private Long tenantId;
    private Long companyId;
    private Long domainId;
    private String domainUrl;
    private String ssoTypeCode;
    private String ssoServerUrl;
    private String ssoLoginUrl;
    private String ssoLogoutUrl;
    private String clientHostUrl;
    private String ssoClientId;
    private String ssoClientPwd;
    private String ssoUserInfo;
    private String samlMetaUrl;
    private String loginNameField;

    public DomainDTO() {
    }

    public Long getDomainId() {
        return this.domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
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

    @Override
    public String toString() {
        return "DomainDTO{" +
                "tenantId=" + tenantId +
                ", companyId=" + companyId +
                ", domainId=" + domainId +
                ", domainUrl='" + domainUrl + '\'' +
                ", ssoTypeCode='" + ssoTypeCode + '\'' +
                ", ssoServerUrl='" + ssoServerUrl + '\'' +
                ", ssoLoginUrl='" + ssoLoginUrl + '\'' +
                ", ssoLogoutUrl='" + ssoLogoutUrl + '\'' +
                ", clientHostUrl='" + clientHostUrl + '\'' +
                ", ssoClientId='" + ssoClientId + '\'' +
                ", ssoClientPwd='" + ssoClientPwd + '\'' +
                ", ssoUserInfo='" + ssoUserInfo + '\'' +
                ", samlMetaUrl='" + samlMetaUrl + '\'' +
                ", loginNameUrl='" + loginNameField + '\'' +
                '}';
    }
}
