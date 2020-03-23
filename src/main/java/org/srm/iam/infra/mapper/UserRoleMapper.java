package org.srm.iam.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.srm.iam.api.dto.UserRoleDTO;
import org.srm.iam.domain.entity.User;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * Mapper
 *
 * @author zhiwei.zhou@hand-china.com 2019-07-19 14:34:30
 */
public interface UserRoleMapper extends BaseMapper<User> {

    /**
     * 根据用户id查询该用户所有的角色以及角色的租户信息
     *
     * @param  organizationId 当前用户所属租户id
     * @param userId 用户id
     * @return UserRoleDTO
     */
    List<UserRoleDTO> listRoleAndTenantByUserId(@Param("organizationId") Long organizationId, @Param("userId") Long userId);

}
