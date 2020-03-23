package org.srm.iam.api.dto;

import java.util.Date;

/**
 * <p>
 * 菜单DTO
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 14:06
 */
public class IamMenuDTO {
    private Long id;
    private String name;
    private String srmFunctionModule;
    private Integer enabledFlag;
    private Date creationDate;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return 名称
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return SRM功能模块名称
     */
    public String getSrmFunctionModule() {
        return srmFunctionModule;
    }

    public void setSrmFunctionModule(String srmFunctionModule) {
        this.srmFunctionModule = srmFunctionModule;
    }

    /**
     * @return 状态，是否启用
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    /**
     * @return 创建时间
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "IamMenuDTO{" + "id=" + id + ", name='" + name + '\'' + ", srmFunctionModule='" + srmFunctionModule
                        + '\'' + ", enableFlag=" + enabledFlag + ", creationDate=" + creationDate + '}';
    }
}
