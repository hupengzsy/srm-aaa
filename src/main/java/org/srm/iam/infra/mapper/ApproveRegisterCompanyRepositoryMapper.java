package org.srm.iam.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.hzero.iam.domain.entity.Tenant;

/**
 * 供应商注册审批mapper
 *
 * @author peng.yang03@hand-china.com 2019/09/17 19:09
 */
public interface ApproveRegisterCompanyRepositoryMapper {

    /**
     * 根据用户id查询租户
     * @param userId 用户id
     * @return 租户
     */
    Tenant getTenantByUserId(Long userId);

    /**
     * 根据用户id查询该用户租户管理员角色id
     *
     * @param userId 用户id
     * @return 租户管理员角色id
     */
    Long selectUserAdminRole(@Param("userId") Long userId);

}
