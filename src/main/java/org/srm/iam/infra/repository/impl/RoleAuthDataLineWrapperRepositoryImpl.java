package org.srm.iam.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.domain.entity.SrmRoleAuthDataLine;
import org.srm.iam.domain.repository.RoleAuthDataLineWrapperRepository;
import org.srm.iam.infra.mapper.RoleAuthDataLineWrapperMapper;

@Component
public class RoleAuthDataLineWrapperRepositoryImpl extends BaseRepositoryImpl<SrmRoleAuthDataLine> implements RoleAuthDataLineWrapperRepository {
    @Autowired
    RoleAuthDataLineWrapperMapper roleAuthDataLineWrapperMapper;
    @Override
    public Page<SrmRoleAuthDataLine> pageCustomer(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pageCustomer(tenantId,roleId,dataCode,dataName);
        });
    }

    @Override
    public Page<SrmRoleAuthDataLine> pageSupplier(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pageSupplier(tenantId,roleId,dataCode,dataName);
        });
    }

    @Override
    public Page<SrmRoleAuthDataLine> pagePurchaseCategory(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pagePurchaseCategory(tenantId,roleId,dataCode,dataName);
        });
    }

    @Override
    public Page<SrmRoleAuthDataLine> pagePurchaseItem(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pagePurchaseItem(tenantId,roleId,dataCode,dataName);
        });
    }

    @Override
    public Page<SrmRoleAuthDataLine> pageSupplierItem(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pageSupplierItem(tenantId,roleId,dataCode,dataName);
        });
    }

    @Override
    public Page<SrmRoleAuthDataLine> pageGroup(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pageGroup(tenantId,roleId,dataCode,dataName);
        });
    }

    @Override
    public Page<SrmRoleAuthDataLine> pageUnit(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pageUnit(tenantId,roleId,dataCode,dataName);
        });
    }

    @Override
    public Page<SrmRoleAuthDataLine> pagePosition(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pagePosition(tenantId,roleId,dataCode,dataName);
        });
    }

    @Override
    public Page<SrmRoleAuthDataLine> pageEmployee(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,()->{
            return roleAuthDataLineWrapperMapper.pageEmployee(tenantId,roleId,dataCode,dataName);
        });
    }

}
