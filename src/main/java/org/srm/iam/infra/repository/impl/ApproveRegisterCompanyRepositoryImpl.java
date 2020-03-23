package org.srm.iam.infra.repository.impl;

import org.hzero.iam.domain.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.domain.repository.ApproveRegisterCompanyRepository;
import org.srm.iam.infra.mapper.ApproveRegisterCompanyRepositoryMapper;

/**
 * 供应商注册repository实现
 *
 * @author peng.yang@hand-china.com 2019/09/17 19:08
 */
@Component
public class ApproveRegisterCompanyRepositoryImpl implements ApproveRegisterCompanyRepository {

    @Autowired
    private ApproveRegisterCompanyRepositoryMapper approveRegisterCompanyRepositoryMapper;

    @Override
    public Tenant getTenantByUserId(Long userId) {
        return approveRegisterCompanyRepositoryMapper.getTenantByUserId(userId);
    }

    @Override
    public Long selectUserAdminRole(Long userId) {
        return approveRegisterCompanyRepositoryMapper.selectUserAdminRole(userId);
    }
}
