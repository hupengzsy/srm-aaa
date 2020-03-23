package org.srm.iam.app.service;

import org.srm.iam.domain.entity.Role;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 10:54
 */
public interface TenantRoleService {

    /**
     * 查询当前租户所有角色
     * 
     * @param organizationId 租户id
     * @return 角色列表
     */
    List<Role> listRoleByOrganizationId(Long organizationId);
}
