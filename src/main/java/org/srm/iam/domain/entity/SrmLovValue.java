//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.srm.iam.domain.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hzero.mybatis.common.query.Where;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

@ApiModel("值集值实体")
@VersionAudit
@ModifyAudit
@MultiLanguage
@Table(
        name = "hpfm_lov_value"
)
@JsonInclude(Include.NON_NULL)
public class SrmLovValue extends AuditDomain {
    public static final String FIELD_LOV_VALUE_ID = "lovValueId";
    public static final String FIELD_LOV_ID = "lovId";
    public static final String FIELD_LOV_CODE = "lovCode";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_MEANING = "meaning";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TAG = "tag";
    public static final String FIELD_ORDER_SEQ = "orderSeq";
    public static final String FIELD_PARENT_VALUE = "parentValue";
    public static final String FIELD_START_DATE_ACTIVE = "startDateActive";
    public static final String FIELD_END_DATE_ACTIVE = "endDateActive";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    @Id
    @GeneratedValue
    @ApiModelProperty("值集值ID")
    private Long lovValueId;
    @ApiModelProperty("值集ID")
    @Where
    private Long lovId;
    @NotEmpty
    @Length(
            max = 60
    )
    @Pattern(
            regexp = "^[a-zA-Z0-9][a-zA-Z0-9-_./]*$"
    )
    @ApiModelProperty("值集代码")
    private String lovCode;
    @NotEmpty
    @Size(
            max = 30
    )
    @ApiModelProperty("值集值")
    private String value;
    @NotEmpty
    @Size(
            max = 120
    )
    @MultiLanguageField
    @ApiModelProperty("含义")
    private String meaning;
    @Size(
            max = 240
    )
    @MultiLanguageField
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @Size(
            max = 240
    )
    @ApiModelProperty("标记")
    private String tag;
    @NotNull
    @ApiModelProperty("排序号")
    private Integer orderSeq;
    @Size(
            max = 30
    )
    @ApiModelProperty("父级值集值")
    private String parentValue;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty("有效期起")
    private Date startDateActive;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty("有效期止")
    private Date endDateActive;
    @NotNull
    @Range(
            min = 0L,
            max = 1L
    )
    @ApiModelProperty("生效标识")
    private Integer enabledFlag;
    @Transient
    @ApiModelProperty("父级含义")
    private String parentMeaning;
    @Transient
    @ApiModelProperty("租户名称")
    private String tenantName;

    public SrmLovValue() {
    }

    public SrmLovValue(String lovCode,String value) {
        this.lovCode = lovCode;
        this.value = value;
    }



    public Long getLovValueId() {
        return this.lovValueId;
    }

    public void setLovValueId(Long lovValueId) {
        this.lovValueId = lovValueId;
    }

    public Long getLovId() {
        return this.lovId;
    }

    public void setLovId(Long lovId) {
        this.lovId = lovId;
    }

    public String getLovCode() {
        return this.lovCode;
    }

    public void setLovCode(String lovCode) {
        this.lovCode = lovCode;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMeaning() {
        return this.meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getOrderSeq() {
        return this.orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getParentValue() {
        return this.parentValue;
    }

    public void setParentValue(String parentValue) {
        this.parentValue = parentValue;
    }

    public Date getStartDateActive() {
        return this.startDateActive;
    }

    public void setStartDateActive(Date startDateActive) {
        this.startDateActive = startDateActive;
    }

    public Date getEndDateActive() {
        return this.endDateActive;
    }

    public void setEndDateActive(Date endDateActive) {
        this.endDateActive = endDateActive;
    }

    public Integer getEnabledFlag() {
        return this.enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getParentMeaning() {
        return this.parentMeaning;
    }

    public void setParentMeaning(String parentMeaning) {
        this.parentMeaning = parentMeaning;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LovValue [lovValueId=");
        builder.append(this.lovValueId);
        builder.append(", lovId=");
        builder.append(this.lovId);
        builder.append(", lovCode=");
        builder.append(this.lovCode);
        builder.append(", value=");
        builder.append(this.value);
        builder.append(", meaning=");
        builder.append(this.meaning);
        builder.append(", description=");
        builder.append(this.description);
        builder.append(", tenantId=");
        builder.append(this.tenantId);
        builder.append(", tag=");
        builder.append(this.tag);
        builder.append(", orderSeq=");
        builder.append(this.orderSeq);
        builder.append(", parentValue=");
        builder.append(this.parentValue);
        builder.append(", startDateActive=");
        builder.append(this.startDateActive);
        builder.append(", endDateActive=");
        builder.append(this.endDateActive);
        builder.append(", enabledFlag=");
        builder.append(this.enabledFlag);
        builder.append(", parentMeaning=");
        builder.append(this.parentMeaning);
        builder.append(", tenantName=");
        builder.append(this.tenantName);
        builder.append("]");
        return builder.toString();
    }
}
