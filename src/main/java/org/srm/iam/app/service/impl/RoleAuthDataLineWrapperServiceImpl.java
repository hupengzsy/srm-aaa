package org.srm.iam.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.iam.app.service.RoleAuthDataLineWrapperService;
import org.srm.iam.domain.entity.SrmRoleAuthDataLine;
import org.srm.iam.domain.repository.RoleAuthDataLineWrapperRepository;

@Service
public class RoleAuthDataLineWrapperServiceImpl implements RoleAuthDataLineWrapperService {

    @Autowired
    RoleAuthDataLineWrapperRepository roleAuthDataLineWrapperRepository;

    @Override
    public Page<SrmRoleAuthDataLine> pageCustomer(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pageCustomer(tenantId,roleId,dataCode,dataName,pageRequest);
    }

    @Override
    public Page<SrmRoleAuthDataLine> pageSupplier(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pageSupplier(tenantId,roleId,dataCode,dataName,pageRequest);
    }

    @Override
    public Page<SrmRoleAuthDataLine> pagePurchaseCategory(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pagePurchaseCategory(tenantId,roleId,dataCode,dataName,pageRequest);

    }

    @Override
    public Page<SrmRoleAuthDataLine> pagePurchaseItem(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pagePurchaseItem(tenantId,roleId,dataCode,dataName,pageRequest);

    }

    @Override
    public Page<SrmRoleAuthDataLine> pageSupplierItem(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pageSupplierItem(tenantId,roleId,dataCode,dataName,pageRequest);

    }

    @Override
    public Page<SrmRoleAuthDataLine> pageGroup(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pageGroup(tenantId,roleId,dataCode,dataName,pageRequest);

    }

    @Override
    public Page<SrmRoleAuthDataLine> pageUnit(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pageUnit(tenantId,roleId,dataCode,dataName,pageRequest);

    }

    @Override
    public Page<SrmRoleAuthDataLine> pagePosition(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pagePosition(tenantId,roleId,dataCode,dataName,pageRequest);

    }

    @Override
    public Page<SrmRoleAuthDataLine> pageEmployee(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return roleAuthDataLineWrapperRepository.pageEmployee(tenantId,roleId,dataCode,dataName,pageRequest);

    }
}
