package org.hzero.iam.app.service;

import org.hzero.iam.api.dto.CompanyOuInvorgDTO;
import org.hzero.iam.domain.entity.UserAuthImport;
import org.hzero.iam.domain.entity.UserAuthority;

import java.util.List;

/**
 * <p>
 * SRM 子账户权限导入
 * </p>
 *
 * @author: shi.yan@hand-china.com 2020/2/20
 */
public interface SrmUserImportService {

    List<CompanyOuInvorgDTO> companyOuInvorgImport(Long tenantId, Long userId, List<CompanyOuInvorgDTO> companyOuInvorgDTOList);

    UserAuthority createOrUpdateUserAuthority(UserAuthImport userAuthImport, Integer includeAllFlag);

    void companyOuInvorgInsertOrUpdate(UserAuthImport userAuthImport);
}
