package org.srm.iam.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.srm.iam.domain.entity.SrmRoleAuthDataLine;

public interface RoleAuthDataLineWrapperService {

    Page<SrmRoleAuthDataLine> pageCustomer(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

    Page<SrmRoleAuthDataLine> pageSupplier(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

    Page<SrmRoleAuthDataLine> pagePurchaseCategory(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

    Page<SrmRoleAuthDataLine> pagePurchaseItem(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

    Page<SrmRoleAuthDataLine> pageSupplierItem(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

    Page<SrmRoleAuthDataLine> pageGroup(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

    Page<SrmRoleAuthDataLine> pageUnit(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

    Page<SrmRoleAuthDataLine> pagePosition(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

    Page<SrmRoleAuthDataLine> pageEmployee(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest);

}
