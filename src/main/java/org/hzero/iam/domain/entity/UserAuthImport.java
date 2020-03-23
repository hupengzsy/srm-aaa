package org.hzero.iam.domain.entity;

import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

import org.hzero.iam.domain.repository.TenantRepository;
import org.hzero.iam.domain.repository.UserRepository;

public class UserAuthImport {
    @ApiModelProperty("唯一标识")
    private String id;
    @ApiModelProperty("类型标识")
    private String typeCode;
    @ApiModelProperty("权限Id")
    private Long authorityId;
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("用户名,excel中头参数")
    private String userName;
    @ApiModelProperty("租户Id")
    private Long tenantId;
    @ApiModelProperty("租户编码,excel中头参数")
    @NotNull
    private String tenantNum;
    @ApiModelProperty("权限类型代码，excel中头参数")
    @NotNull
    private String authorityTypeCode;
    @ApiModelProperty("权限类型标识，excel中头参数")
    private Long includeAllFlag;
    @ApiModelProperty("行Id")
    private Long authorityLineId;
    @ApiModelProperty("数据id")
    private Long dataId;
    @ApiModelProperty("数据编码,excel中行参数")
    @NotNull
    private String dataCode;
    @ApiModelProperty("数据名称,excel中行参数")
    private String dataName;
    @ApiModelProperty("名称")
    private String realName;

    public UserAuthImport() {
    }

    public void validParam(UserRepository userRepository, TenantRepository tenantRepository) {
        User queryUser = new User();
        queryUser.setLoginName(this.getUserName());
        queryUser.setRealName(this.getRealName());
        queryUser = (User) userRepository.selectOne(queryUser);
        if (queryUser == null) {
            throw new CommonException("user.import.user_login_name_not_exsit");
        } else {
            this.setUserId(queryUser.getId());
            Tenant tenant = new Tenant();
            tenant.setTenantNum(this.getTenantNum());
            tenant = tenantRepository.selectOne(tenant);
            if (tenant == null) {
                throw new CommonException("user.import.tenant_num_not_exsit");
            } else {
                this.setTenantId(tenant.getTenantId());
            }
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getAuthorityTypeCode() {
        return this.authorityTypeCode;
    }

    public void setAuthorityTypeCode(String authorityTypeCode) {
        this.authorityTypeCode = authorityTypeCode;
    }

    public Long getIncludeAllFlag() {
        return this.includeAllFlag;
    }

    public void setIncludeAllFlag(Long includeAllFlag) {
        this.includeAllFlag = includeAllFlag;
    }

    public Long getAuthorityLineId() {
        return this.authorityLineId;
    }

    public void setAuthorityLineId(Long authorityLineId) {
        this.authorityLineId = authorityLineId;
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

    public Long getAuthorityId() {
        return this.authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getTypeCode() {
        return this.typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantNum() {
        return this.tenantNum;
    }

    public void setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
