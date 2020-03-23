package org.srm.iam.domain.repository;

import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.entity.User;
import org.srm.iam.api.dto.TenantEsDTO;
import org.srm.iam.domain.entity.UserEs;

import java.util.List;

/**
 * 公司导入repository
 *
 * @author peng.yang03@hand-china.com 2019/10/31 15:38
 */
public interface CompanyImportRepository {

    /**
     * 根据外部系统租户id 查询本系统数据
     *
     * @param tenantEs 租户关系
     * @return 查询结果
     */
    List<TenantEsDTO> queryTenantId(List<TenantEsDTO> tenantEs);

    /**
     * 根据外部系统用户id 查询本系统数据 确定是新建/更新的数据
     *
     * @param userEs 用户es
     * @return 已同步过的数据
     */
    List<UserEs> queryExistUserEs(List<UserEs> userEs);

    /**
     * 根据租户id 查询所有的角色信息
     *
     * @param tenantIdList 租户id
     * @return 角色
     */
    List<Role> queryAllRoleByTenantId(List<Long> tenantIdList);

    /**
     * 查找租户管理员用户
     *
     * @param tenantIds 租户id
     * @return 用户信息
     */
    List<User> queryManagerByRole(List<Long> tenantIds);

}
