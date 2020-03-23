package org.srm.iam.api.dto;


import org.hzero.core.base.BaseConstants;
import org.srm.iam.domain.vo.SslmCompanyVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 角色权限分配数据DTO,支持链式set
 * </p>
 *
 * @author mingwei.liu@hand-china.com 2018/7/11
 */
public class TenantRoleAutoAssignmentDTO {

    public TenantRoleAutoAssignmentDTO(){}

    public TenantRoleAutoAssignmentDTO(SslmCompanyVO company) {
        this.userId = company.getCreatedBy();
        this.tenantId = company.getTenantId();
        this.newTenantFlag = Objects.equals(company.getNewTenantFlag(), BaseConstants.Flag.YES);
        this.companyNum = company.getCompanyNum();
        this.sourceKey = company.getSourceKey();
        this.sourceCode= company.getSourceCode();
    }

    /**
     * 角色模板
     *
     * @author gaokuo.dai@hand-china.com 2018年10月31日上午11:31:31
     */
    public enum RoleTemplateCode{

        /**
         * 租户管理员
         */
        DEFAULT_TENANT_ADMIN("role/site/custom/default/tenantadministrator"),
        /**
         * 销售管理员
         */
        DEFAULT_SALER_ADMIN("role/organization/default/template/sales"),
        /**
         * 专家
         */
        DEFAULT_EXPERT("role/organization/default/template/experts");

        private String code;

        private RoleTemplateCode(String code) {
            this.code = code;
        }

        /**
         * @return 角色代码
         */
        public String getCode() {
            return this.code;
        }
    }

    private Long userId;
    private Long tenantId;
    private Boolean newTenantFlag;
    private String companyNum;
    private String sourceKey;
    private String sourceCode;
    private List<String> roleTemplateCodes;
    
    /**
     * @return 管理员用户ID
     */
    public Long getUserId() {
        return userId;
    }
    public TenantRoleAutoAssignmentDTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }
    public TenantRoleAutoAssignmentDTO setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    /**
     * @return 是否为新建租户
     */
    public Boolean isNewTenantFlag() {
        return newTenantFlag;
    }
    public TenantRoleAutoAssignmentDTO setNewTenantFlag(Boolean newTenantFlag) {
        this.newTenantFlag = newTenantFlag;
        return this;
    }
    /**
     * @return 公司编码
     */
    public String getCompanyNum() {
        return companyNum;
    }
    public TenantRoleAutoAssignmentDTO setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
        return this;
    }
    /**
     * @return 数据来源
     */
    public String getSourceCode() {
        return sourceCode;
    }
    public TenantRoleAutoAssignmentDTO setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
        return this;
    }
    /**
     * @return 源数据key
     */
    public String getSourceKey() {
        return sourceKey;
    }
    public TenantRoleAutoAssignmentDTO setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
        return this;
    }
    /**
     * @return 租户管理员基础角色模板
     */
    public List<String> getRoleTemplateCodes() {
        return roleTemplateCodes;
    }
    public TenantRoleAutoAssignmentDTO setRoleTemplateCodes(List<String> roleTemplateCodes) {
        this.roleTemplateCodes = roleTemplateCodes;
        return this;
    }
    
    /**
     * 添加一个角色模板代码
     *
     * @param roleTemplateCode 角色模板代码
     * @return TenantRoleAutoAssignmentDTO
     */
    public TenantRoleAutoAssignmentDTO addRoleTemplateCode(String roleTemplateCode) {
        if(this.roleTemplateCodes == null) {
            this.roleTemplateCodes = new ArrayList<>();
        }
        this.roleTemplateCodes.add(roleTemplateCode);
        return this;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TenantAdminRoleAndDataPrivAutoAssignmentDTO [userId=");
        builder.append(userId);
        builder.append(", tenantId=");
        builder.append(tenantId);
        builder.append(", newTenantFlag=");
        builder.append(newTenantFlag);
        builder.append(", companyNum=");
        builder.append(companyNum);
        builder.append(", sourceKey=");
        builder.append(sourceKey);
        builder.append(", sourceCode=");
        builder.append(sourceCode);
        builder.append(", roleTemplateCodes=");
        builder.append(roleTemplateCodes);
        builder.append("]");
        return builder.toString();
    }
    
}

