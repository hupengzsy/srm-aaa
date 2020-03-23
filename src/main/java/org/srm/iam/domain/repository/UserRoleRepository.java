package org.srm.iam.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.iam.api.dto.UserRoleDTO;
import org.srm.iam.domain.entity.User;

import java.util.List;

/**
 * 资源库
 *
 * @author zhiwei.zhou@hand-china.com 2019-07-19 14:34:30
 */
public interface UserRoleRepository extends BaseRepository<User> {

    /**
     * 根据用户id查询当前用户所有租户及角色
     * 
     * @param organizationId 用户所属租户
     * @param id 用户id
     * @return List<UserRoleDTO>
     */
    List<UserRoleDTO> listRoleAndTenantByUserId(Long organizationId, Long id);

}
