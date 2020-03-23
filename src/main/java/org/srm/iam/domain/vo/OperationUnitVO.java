package org.srm.iam.domain.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author jianbo.li@hand-china.com 2018-09-19 14:29:49
 */
public class OperationUnitVO {
    //
    // 数据库字段
    // ------------------------------------------------------------------------------
    private Long ouId;
    private String ouCode;
    private String ouName;
    private Long companyId;
    private String sourceCode;
    private Long tenantId;
    private Integer enabledFlag;
    private Long objectVersionNumber;
    private String companyName;
    //
    // 业务方法
    // ------------------------------------------------------------------------------

    /**
     * 获取指定ID的业务实体
     * @param operationUnitVOList
     * @param companyId
     * @return
     */
    public static List<OperationUnitVO> getUnitsByCompanyId(List<OperationUnitVO> operationUnitVOList,Long companyId){
        List<OperationUnitVO> result=new ArrayList<>();
        for (OperationUnitVO operationUnitVO:operationUnitVOList){
            if(Objects.equals(operationUnitVO.getCompanyId(),companyId)){
                result.add(operationUnitVO);
            }
        }
        return result;
    }

    public Long getOuId() {
        return ouId;
    }

    public void setOuId(Long ouId) {
        this.ouId = ouId;
    }

    public String getOuCode() {
        return ouCode;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getOuName() {
        return ouName;
    }

    public void setOuName(String ouName) {
        this.ouName = ouName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
