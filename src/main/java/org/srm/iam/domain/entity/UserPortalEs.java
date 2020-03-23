package org.srm.iam.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户二级域名关联表
 *
 * @author shi.yan@hand-china.com 2020-02-24 17:19:40
 */
@ApiModel("用户二级域名关联表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "iam_user_portal_es")
public class UserPortalEs extends AuditDomain {

    public static final String FIELD_USER_PORTAL_ES_ID = "userPortalEsId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public UserPortalEs() {
    }

    public UserPortalEs(Long userId, Long tenantId) {
        this.userId = userId;
        this.tenantId = tenantId;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long userPortalEsId;
    @ApiModelProperty(value = "用户ID，iam_user表主键", required = true)
    @NotNull
    private Long userId;
    @ApiModelProperty(value = "二级域名对应租户ID", required = true)
    @NotNull
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getUserPortalEsId() {
        return userPortalEsId;
    }

    public void setUserPortalEsId(Long userPortalEsId) {
        this.userPortalEsId = userPortalEsId;
    }

    /**
     * @return 用户ID，iam_user表主键
     */
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public UserPortalEs setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    @Override
    public String toString() {
        return "UserPortalEs{" +
                "userPortalEsId=" + userPortalEsId +
                ", userId=" + userId +
                ", tenantId=" + tenantId +
                '}';
    }
}
