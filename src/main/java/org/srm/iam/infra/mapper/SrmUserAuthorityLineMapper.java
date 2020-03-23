

package org.srm.iam.infra.mapper;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hzero.iam.api.dto.UserAuthorityDataDTO;
import org.hzero.iam.domain.entity.UserAuthorityLine;
import org.srm.iam.domain.entity.SrmUserAuthorityLine;

import java.util.List;

public interface SrmUserAuthorityLineMapper extends BaseMapper<SrmUserAuthorityLine> {

    Page<UserAuthorityLine> selectCreateUserAuthorityLines(@Param("authorityId") Long authorityId, @Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    void updateUserAuthorityLine(@Param("tenantId") Long tenantId, @Param("authorityId") Long authorityId, @Param("copyAuthorityId") Long copyAuthorityId);

    List<UserAuthorityDataDTO> listCustomers(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<UserAuthorityDataDTO> listSuppliers(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<UserAuthorityDataDTO> listPurOrg(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<UserAuthorityDataDTO> listPurAgent(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<UserAuthorityDataDTO> listPurCat(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<UserAuthorityDataDTO> listLov(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<UserAuthorityDataDTO> listLovView(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<UserAuthorityDataDTO> listDatasource(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<UserAuthorityDataDTO> listDataGroup(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("groupCode") String groupCode, @Param("groupName") String groupName);

    List<UserAuthorityDataDTO> listPosition(Long tenantId, Long userId, String dataCode, String dataName);

    List<UserAuthorityDataDTO> listGroup(Long tenantId, Long userId, String dataCode, String dataName);

    List<UserAuthorityDataDTO> listEmployee(Long tenantId, Long userId, String dataCode, String dataName);

    List<UserAuthorityDataDTO> listUnit(Long tenantId, Long userId, String dataCode, String dataName);

    List<UserAuthorityDataDTO> listPurchaseItem(Long tenantId, Long userId, String dataCode, String dataName);

    List<UserAuthorityLine> selectByAuthorityIdAndTenantId(@Param("authorityId") Long authorityId, @Param("tenantId") Long tenantId);

}
