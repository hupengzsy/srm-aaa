package org.srm.iam.api.dto;

public class RoleAuthorityDataWrapperDTO {
    public static final String FIELD_DATA_ID = "dataId";
    public static final String FIELD_DATA_NAME = "dataName";
    public static final String FIELD_DATA_CODE = "dataCode";
    private Long dataId;
    private String dataName;
    private String dataCode;

    public RoleAuthorityDataWrapperDTO() {
    }

    public Long getDataId() {
        return this.dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getDataName() {
        return this.dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataCode() {
        return this.dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }
}
