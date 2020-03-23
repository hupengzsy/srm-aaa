package org.srm.iam.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.springframework.stereotype.Component;
import org.srm.iam.domain.entity.RoleAuthDataLineWrapper;
import org.srm.iam.domain.entity.SrmRoleAuthDataLine;

import java.util.List;

@Component
public interface RoleAuthDataLineWrapperMapper extends BaseMapper<RoleAuthDataLineWrapper> {

    List<SrmRoleAuthDataLine> pageCustomer(Long tenantId, Long roleId, String dataCode, String dataName) ;

    List<SrmRoleAuthDataLine> pageSupplier(Long tenantId, Long roleId, String dataCode, String dataName) ;

    List<SrmRoleAuthDataLine> pagePurchaseCategory(Long tenantId, Long roleId, String dataCode, String dataName) ;

    List<SrmRoleAuthDataLine> pagePurchaseItem(Long tenantId, Long roleId, String dataCode, String dataName) ;

    List<SrmRoleAuthDataLine> pageSupplierItem(Long tenantId, Long roleId, String dataCode, String dataName) ;

    List<SrmRoleAuthDataLine> pageGroup(Long tenantId, Long roleId, String dataCode, String dataName) ;

    List<SrmRoleAuthDataLine> pageUnit(Long tenantId, Long roleId, String dataCode, String dataName) ;

    List<SrmRoleAuthDataLine> pagePosition(Long tenantId, Long roleId, String dataCode, String dataName) ;

    List<SrmRoleAuthDataLine> pageEmployee(Long tenantId, Long roleId, String dataCode, String dataName) ;


}

