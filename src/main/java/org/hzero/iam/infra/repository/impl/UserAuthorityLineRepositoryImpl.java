//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hzero.iam.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.iam.api.dto.UserAuthorityDataDTO;
import org.hzero.iam.domain.entity.UserAuthorityLine;
import org.hzero.iam.domain.repository.UserAuthorityLineRepository;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.infra.mapper.SrmUserAuthorityLineMapper;

import java.util.List;

@Component
public class UserAuthorityLineRepositoryImpl extends BaseRepositoryImpl<UserAuthorityLine> implements UserAuthorityLineRepository {
    @Autowired
    private SrmUserAuthorityLineMapper srmUserAuthorityLineMapper;

    public UserAuthorityLineRepositoryImpl() {
    }

    @Override
    public Page<UserAuthorityLine> selectCreateUserAuthorityLines(Long authorityId, Long tenantId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.selectCreateUserAuthorityLines(authorityId, tenantId, dataCode, dataName);
        });
    }

    @Override
    public void updateUserAuthorityLine(Long tenantId, Long authorityId, Long copyAuthorityId) {
        this.srmUserAuthorityLineMapper.updateUserAuthorityLine(tenantId, authorityId, copyAuthorityId);
    }

    @Override
    public Page<UserAuthorityDataDTO> listPurCat(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listPurCat(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> pageLov(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listLov(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> pageLovView(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listLovView(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> pageDatasource(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listDatasource(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listPosition(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listPosition(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listGroup(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listGroup(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listEmployee(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listEmployee(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listUnit(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            List<UserAuthorityDataDTO> userAuthorityDataDTOS = this.srmUserAuthorityLineMapper.listUnit(tenantId, userId, dataCode, dataName);
            return userAuthorityDataDTOS;
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> pageGroupData(Long tenantId, Long userId, String groupCode, String groupName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listDataGroup(tenantId, userId, groupCode, groupName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listPurAgent(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listPurAgent(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listPurOrg(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listPurOrg(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listSuppliers(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listSuppliers(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listCustomers(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listCustomers(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public Page<UserAuthorityDataDTO> listPurchaseItem(Long tenantId, Long userId, String dataCode, String dataName, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> {
            return this.srmUserAuthorityLineMapper.listPurchaseItem(tenantId, userId, dataCode, dataName);
        });
    }

    @Override
    public List<UserAuthorityLine> selectByAuthorityIdAndTenantId(Long authorityId, Long tenantId) {
        return this.srmUserAuthorityLineMapper.selectByAuthorityIdAndTenantId(authorityId, tenantId);
    }

}
