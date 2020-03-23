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
 * 用户关联表
 *
 * @author peng.yang03@hand-china.com 2019-10-31 14:38:15
 */
@ApiModel("用户关联表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "iam_user_es")
public class UserEs extends AuditDomain {

    public static final String FIELD_USER_ES_ID = "userEsId";
    public static final String FIELD_EXTERNAL_SYSTEM_CODE = "externalSystemCode";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_ES_USER_ID = "esUserId";
    public static final String FIELD_DATA_VERSION = "dataVersion";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

	public UserEs() {}

	public UserEs(@NotBlank String externalSystemCode, String esUserId) {
		this.externalSystemCode = externalSystemCode;
		this.esUserId = esUserId;
	}

	public UserEs(@NotBlank String externalSystemCode, Long userId, String esUserId, @NotNull Long dataVersion) {
		this.externalSystemCode = externalSystemCode;
		this.userId = userId;
		this.esUserId = esUserId;
		this.dataVersion = dataVersion;
	}

	//
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private Long userEsId;
    @ApiModelProperty(value = "外部系统代码",required = true)
    @NotBlank
    private String externalSystemCode;
   @ApiModelProperty(value = "srm用户id")    
    private Long userId;
   @ApiModelProperty(value = "外部系统用户id")    
    private String esUserId;
    @ApiModelProperty(value = "外部系统数据版本号",required = true)
    @NotNull
    private Long dataVersion;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getUserEsId() {
		return userEsId;
	}

	public void setUserEsId(Long userEsId) {
		this.userEsId = userEsId;
	}
    /**
     * @return 外部系统代码
     */
	public String getExternalSystemCode() {
		return externalSystemCode;
	}

	public void setExternalSystemCode(String externalSystemCode) {
		this.externalSystemCode = externalSystemCode;
	}
    /**
     * @return srm用户id
     */
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
    /**
     * @return 外部系统用户id
     */
	public String getEsUserId() {
		return esUserId;
	}

	public void setEsUserId(String esUserId) {
		this.esUserId = esUserId;
	}
    /**
     * @return 外部系统数据版本号
     */
	public Long getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(Long dataVersion) {
		this.dataVersion = dataVersion;
	}

}
