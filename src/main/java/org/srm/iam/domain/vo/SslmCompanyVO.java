package org.srm.iam.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.domain.AuditDomain;
import org.hzero.core.base.BaseConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 公司信息
 *
 * @author gaokuo.dai@hand-china.com 2018-07-04 19:49:15
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SslmCompanyVO extends AuditDomain {

    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_COMPANY_NUM = "companyNum";
    public static final String FIELD_GROUP_ID = "groupId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_UNIT_ID = "unitId";
    public static final String FIELD_DOMESTIC_FOREIGN_RELATION = "domesticForeignRelation";
    public static final String FIELD_UNIFIED_SOCIAL_CODE = "unifiedSocialCode";
    public static final String FIELD_ORGANIZING_INSTITUTION_CODE = "organizingInstitutionCode";
    public static final String FIELD_COMPANY_NAME = "companyName";
    public static final String FIELD_SHORT_NAME = "shortName";
    public static final String FIELD_COMPANY_TYPE = "companyType";
    public static final String FIELD_REGISTERED_COUNTRY_ID = "registeredCountryId";
    public static final String FIELD_REGISTERED_REGION_ID = "registeredRegionId";
    public static final String FIELD_ADDRESS_DETAIL = "addressDetail";
    public static final String FIELD_DUNS_CODE = "dunsCode";
    public static final String FIELD_TAXPAYER_TYPE = "taxpayerType";
    public static final String FIELD_LEGAL_REP_NAME = "legalRepName";
    public static final String FIELD_BUILD_DATE = "buildDate";
    public static final String FIELD_REGISTERED_CAPITAL = "registeredCapital";
    public static final String FIELD_LICENCE_END_DATE = "licenceEndDate";
    public static final String FIELD_BUSINESS_SCOPE = "business_scope";
    public static final String FIELD_LONG_TERM_FLAG = "longTermFlag";
    public static final String FIELD_LICENCE_URL = "licenceUrl";
    public static final String FIELD_SOURCE_KEY = "sourceKey";
    public static final String FIELD_SOURCE_CODE = "sourceCode";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_COMPANY_BASIC_ID = "companyBasicId";


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 字段
    // ------------------------------------------------------------------------------

    private Long companyId;
    private String companyNum;
    private Long companyBasicId;
    private Long groupId;
    private Long tenantId;
    private Long unitId;
    private String domesticForeignRelation;
    private String unifiedSocialCode;
    private String organizingInstitutionCode;
    private String companyName;
    private String shortName;
    private String companyType;
    private Long registeredCountryId;
    private Long registeredRegionId;
    private String addressDetail;
    private String dunsCode;
    private String taxpayerType;
    private String legalRepName;
    @JsonFormat(pattern = BaseConstants.Pattern.DATE)
    @DateTimeFormat(pattern = BaseConstants.Pattern.DATE)
    private LocalDate buildDate;
    private BigDecimal registeredCapital;
    @JsonFormat(pattern = BaseConstants.Pattern.DATE)
    @DateTimeFormat(pattern = BaseConstants.Pattern.DATE)
    private LocalDate licenceEndDate;
    private String businessScope;
    private Integer longTermFlag;
    private String licenceUrl;
    private String sourceKey;
    private String sourceCode;
    private Integer enabledFlag;
    private Integer newTenantFlag;
    private Long remoteCompanyId;
    private List<OperationUnitVO> children;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID
     */
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * @return 公司编码，自动生成
     */
    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    /**
     * @return 集团id
     */
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * @return 租户id，从集团带过来
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 组织id
     */
    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    /**
     * @return 境内境外，IN境内，OUT境外
     */
    public String getDomesticForeignRelation() {
        return domesticForeignRelation;
    }

    public void setDomesticForeignRelation(String domesticForeignRelation) {
        this.domesticForeignRelation = domesticForeignRelation;
    }

    /**
     * @return 统一社会信用码
     */
    public String getUnifiedSocialCode() {
        return unifiedSocialCode;
    }

    public void setUnifiedSocialCode(String unifiedSocialCode) {
        this.unifiedSocialCode = unifiedSocialCode;
    }

    /**
     * @return 组织机构代码，和统一社会信用码至少存在一个
     */
    public String getOrganizingInstitutionCode() {
        return organizingInstitutionCode;
    }

    public void setOrganizingInstitutionCode(String organizingInstitutionCode) {
        this.organizingInstitutionCode = organizingInstitutionCode;
    }

    /**
     * @return 公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return 公司简称
     */
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * @return 公司类型
     */
    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    /**
     * @return 国家
     */
    public Long getRegisteredCountryId() {
        return registeredCountryId;
    }

    public void setRegisteredCountryId(Long registeredCountryId) {
        this.registeredCountryId = registeredCountryId;
    }

    /**
     * @return 地区id，树形值集
     */
    public Long getRegisteredRegionId() {
        return registeredRegionId;
    }

    public void setRegisteredRegionId(Long registeredRegionId) {
        this.registeredRegionId = registeredRegionId;
    }

    /**
     * @return 详细地址
     */
    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    /**
     * @return 邓白氏编码
     */
    public String getDunsCode() {
        return dunsCode;
    }

    public void setDunsCode(String dunsCode) {
        this.dunsCode = dunsCode;
    }

    /**
     * @return 纳税人类型，值集HPFM.TAXPAYER_TYPE
     */
    public String getTaxpayerType() {
        return taxpayerType;
    }

    public void setTaxpayerType(String taxpayerType) {
        this.taxpayerType = taxpayerType;
    }

    /**
     * @return 法人姓名
     */
    public String getLegalRepName() {
        return legalRepName;
    }

    public void setLegalRepName(String legalRepName) {
        this.legalRepName = legalRepName;
    }

    /**
     * @return 成立日期
     */
    public LocalDate getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(LocalDate buildDate) {
        this.buildDate = buildDate;
    }

    /**
     * @return 注册资本
     */
    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    /**
     * @return 营业期限
     */
    public LocalDate getLicenceEndDate() {
        return licenceEndDate;
    }

    public void setLicenceEndDate(LocalDate licenceEndDate) {
        this.licenceEndDate = licenceEndDate;
    }

    /**
     * @return 经营范围
     */
    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    /**
     * @return 长期标志，1：长期，0：非长期
     */
    public Integer getLongTermFlag() {
        return longTermFlag;
    }

    public void setLongTermFlag(Integer longTermFlag) {
        this.longTermFlag = longTermFlag;
    }

    /**
     * @return 营业执照附件路径
     */
    public String getLicenceUrl() {
        return licenceUrl;
    }

    public void setLicenceUrl(String licenceUrl) {
        this.licenceUrl = licenceUrl;
    }

    /**
     * @return 源数据key
     */
    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    /**
     * @return 来源, 值集：HPFM.DATA_SOURCE
     */
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
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

    /**
     * @return SPRM公司基础ID
     */
    public Long getCompanyBasicId() {
        return companyBasicId;
    }

    public void setCompanyBasicId(Long companyBasicId) {
        this.companyBasicId = companyBasicId;
    }

    /**
     * @return 是否新建租户
     */
    public Integer getNewTenantFlag() {
        return newTenantFlag;
    }

    public void setNewTenantFlag(Integer newTenantFlag) {
        this.newTenantFlag = newTenantFlag;
    }

    /**
     * @return 远程公司ID
     */
    public Long getRemoteCompanyId() {
        return remoteCompanyId;
    }

    public void setRemoteCompanyId(Long remoteCompanyId) {
        this.remoteCompanyId = remoteCompanyId;
    }

    public List<OperationUnitVO> getChildren() {
        return children;
    }

    public void setChildren(List<OperationUnitVO> children) {
        this.children = children;
    }
}
