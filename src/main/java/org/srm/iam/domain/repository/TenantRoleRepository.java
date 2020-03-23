package org.srm.iam.domain.repository;

import java.util.List;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.iam.domain.entity.Role;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 10:55
 */
public interface TenantRoleRepository extends BaseRepository<Role> {

    /**
     * 根据租户id查询角色
     * 
     * @param organizationId 租户id
     * @return 角色列表
     */
    List<Role> selectRoleByOrganizationId(Long organizationId);
}
