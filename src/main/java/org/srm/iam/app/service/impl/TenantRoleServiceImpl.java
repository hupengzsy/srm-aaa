package org.srm.iam.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.iam.app.service.TenantRoleService;
import org.srm.iam.domain.entity.Role;
import org.srm.iam.domain.repository.TenantRoleRepository;

import java.util.List;

/**
 * <p>
 * 服务实现
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 10:55
 */
@Service
public class TenantRoleServiceImpl implements TenantRoleService {
    private final TenantRoleRepository tenantRoleRepository;

    @Autowired
    public TenantRoleServiceImpl(TenantRoleRepository tenantRoleRepository) {
        this.tenantRoleRepository = tenantRoleRepository;
    }

    @Override
    public List<Role> listRoleByOrganizationId(Long organizationId) {
        return tenantRoleRepository.selectRoleByOrganizationId(organizationId);
    }
}
