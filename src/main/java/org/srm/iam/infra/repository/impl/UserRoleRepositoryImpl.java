package org.srm.iam.infra.repository.impl;

import java.util.List;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.api.dto.UserRoleDTO;
import org.srm.iam.domain.entity.User;
import org.srm.iam.domain.repository.UserRoleRepository;
import org.srm.iam.infra.mapper.UserRoleMapper;

/**
 * 资源库实现
 *
 * @author zhiwei.zhou@hand-china.com 2019-07-19 14:34:30
 */
@Component
public class UserRoleRepositoryImpl extends BaseRepositoryImpl<User> implements UserRoleRepository {
    private final UserRoleMapper userRoleMapper;

    @Autowired
    public UserRoleRepositoryImpl(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }


    @Override
    public List<UserRoleDTO> listRoleAndTenantByUserId(Long organizationId, Long id) {
        return userRoleMapper.listRoleAndTenantByUserId(organizationId, id);
    }
}
