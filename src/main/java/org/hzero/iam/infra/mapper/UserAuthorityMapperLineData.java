package org.hzero.iam.infra.mapper;


import org.apache.ibatis.annotations.Param;
import org.hzero.iam.api.dto.CompanyOuInvorgNodeDTO;
import org.hzero.iam.domain.vo.CompanyVO;

import java.util.List;

public interface UserAuthorityMapperLineData {

    List<CompanyOuInvorgNodeDTO> listComanyUoInvorg(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<String> listUserAuthorityTypeCode(@Param("tenantId") Long tenantId, @Param("userId") Long userId);

    Long queryComDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryOUDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryInvDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryCusDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long querySupDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryPOrgDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryPAgentDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryPCatDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    CompanyVO selectCompanyInfo(@Param("companyNum") String companyNum, @Param("tenantId") Long tenantId);

    Long queryGroupDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryUnitDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryPositionDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryEmployeeDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long queryPItemDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    Long querySalItemDataSourceInfo(@Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);
}
