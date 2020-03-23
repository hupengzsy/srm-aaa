package org.srm.iam.infra.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.api.dto.TenantEsDTO;
import org.srm.iam.domain.entity.UserEs;
import org.srm.iam.domain.repository.CompanyImportRepository;
import org.srm.iam.infra.mapper.CompanyImportMapper;

import java.util.List;

/**
 * 公司导入repository实现
 *
 * @author peng.yang03@hand-china.com 2019/10/31 15:39
 */
@Component
public class CompanyImportRepositoryImpl implements CompanyImportRepository {

    @Autowired
    private CompanyImportMapper companyImportMapper;

    @Override
    public List<TenantEsDTO> queryTenantId(List<TenantEsDTO> tenantEs) {
        return companyImportMapper.queryTenantId(tenantEs);
    }

    @Override
    public List<UserEs> queryExistUserEs(List<UserEs> userEsList) {
        return companyImportMapper.queryExistUserEs(userEsList);
    }

    @Override
    public List<Role> queryAllRoleByTenantId(List<Long> tenantIdList) {
        return companyImportMapper.queryAllRoleByTenantId(tenantIdList);
    }

    @Override
    public List<User> queryManagerByRole(List<Long> tenantIds) {
        return companyImportMapper.queryManagerByRole(tenantIds);
    }

}
