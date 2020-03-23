package org.srm.iam.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.domain.entity.Role;
import org.srm.iam.domain.repository.TenantRoleRepository;
import org.srm.iam.infra.mapper.TenantRoleMapper;

import java.util.List;

/**
 * <p>
 * 资源库实现
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 10:57
 */
@Component
public class TenantRoleRepositoryImpl extends BaseRepositoryImpl<Role> implements TenantRoleRepository {
    private final TenantRoleMapper tenantRoleMapper;

    @Autowired
    public TenantRoleRepositoryImpl(TenantRoleMapper tenantRoleMapper) {
        this.tenantRoleMapper = tenantRoleMapper;
    }

    @Override
    public List<Role> selectRoleByOrganizationId(Long organizationId) {
        return tenantRoleMapper.selectRoleByOrganizationId(organizationId);
    }
}
