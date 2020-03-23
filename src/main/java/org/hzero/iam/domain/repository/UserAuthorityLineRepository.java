//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hzero.iam.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.iam.api.dto.UserAuthorityDataDTO;
import org.hzero.iam.domain.entity.UserAuthorityLine;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

public interface UserAuthorityLineRepository extends BaseRepository<UserAuthorityLine> {
    Page<UserAuthorityLine> selectCreateUserAuthorityLines(Long authorityId, Long tenantId, String dataCode, String dataName, PageRequest pageRequest);

    void updateUserAuthorityLine(Long tenantId, Long authorityId, Long copyAuthorityId);

    Page<UserAuthorityDataDTO> listCustomers(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listSuppliers(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listPurOrg(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listPurAgent(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listPurCat(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> pageLov(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> pageLovView(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> pageDatasource(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> pageGroupData(Long tenantId, Long userId, String groupCode, String groupName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listPosition(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listGroup(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listEmployee(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listUnit(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    Page<UserAuthorityDataDTO> listPurchaseItem(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest);

    List<UserAuthorityLine> selectByAuthorityIdAndTenantId(Long authorityId, Long tenantId);

}
