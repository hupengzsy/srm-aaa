//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hzero.iam.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import java.util.List;
import org.hzero.iam.domain.entity.RoleAuthDataLine;
import org.hzero.iam.domain.repository.RoleAuthDataLineRepository;
import org.hzero.iam.infra.mapper.RoleAuthDataLineMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.infra.mapper.SrmRoleAuthDataLineMapper;

@Component
public class RoleAuthDataLineRepositoryImpl extends BaseRepositoryImpl<RoleAuthDataLine> implements RoleAuthDataLineRepository {

    @Autowired
    private SrmRoleAuthDataLineMapper roleAuthDataLineMapper;


    @Override
    public List<RoleAuthDataLine> pageRoleAuthDataLine(Long authDataId, Long tenantId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.roleAuthDataLineMapper.listRoleAuthDataLine(authDataId, tenantId, dataCode, dataName);
        });
    }

    @Override
    public Page<RoleAuthDataLine> pagePurOrg(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.roleAuthDataLineMapper.listRoleAuthDataLinePurOrg(tenantId, roleId, dataCode, dataName);
        });
    }

    @Override
    public Page<RoleAuthDataLine> pagePurAgent(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.roleAuthDataLineMapper.listRoleAuthDataLinePurAgent(tenantId, roleId, dataCode, dataName);
        });
    }

    @Override
    public Page<RoleAuthDataLine> pageLov(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.roleAuthDataLineMapper.listRoleAuthDataLineLov(tenantId, roleId, dataCode, dataName);
        });
    }

    @Override
    public Page<RoleAuthDataLine> pageLovView(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.roleAuthDataLineMapper.listRoleAuthDataLineLovView(tenantId, roleId, dataCode, dataName);
        });
    }

    @Override
    public Page<RoleAuthDataLine> pageDatasource(Long tenantId, Long roleId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.roleAuthDataLineMapper.listRoleAuthDataLineDatasource(tenantId, roleId, dataCode, dataName);
        });
    }

    @Override
    public Page<RoleAuthDataLine> pageDataGroup(Long tenantId, Long roleId, String groupCode, String groupName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.roleAuthDataLineMapper.listRoleAuthGroupData(tenantId, roleId, groupCode, groupName);
        });
    }
}
