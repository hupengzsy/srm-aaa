package org.srm.iam.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.vo.RoleVO;
import org.srm.iam.domain.entity.SrmRole;

import java.util.List;
import java.util.Set;

/**
 * description
 *
 * @author mingzhi.li@hand-china.com
 */

public interface SrmRoleMapper extends BaseMapper<SrmRole> {
    List<Role> selectAllParentRoles(@Param("roleId") Long roleId);

    List<Role> selectAllSubRoles(@Param("roleId") Long roleId);

    RoleVO selectRoleDetails(@Param("roleId") Long roleId);

    List<RoleVO> selectMemberRoles(RoleVO params);

    List<Role> selectInheritSubRolesWithPermissionSets(@Param("inheritRoleId") Long inheritRoleId, @Param("permissionSetIds") Set<Long> permissionSetIds, @Param("permissionType") String permissionType);

    List<Role> selectCreatedSubRolesWithPermissionSets(@Param("parentRoleId") Long parentRoleId, @Param("permissionSetIds") Set<Long> permissionSetIds, @Param("permissionType") String permissionType);

    int countUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    List<RoleVO> selectUserAdminRoles(RoleVO params);

    List<RoleVO> selectUserManageableRoles(RoleVO params);

    List<String> selectParentManageRoles(RoleVO params);

    List<RoleVO> selectSimpleRoles(RoleVO params);

    List<Role> selectRoleAssignInfo(Role params);

    RoleVO selectAdminRole(RoleVO params);

    List<Role> listTenantAdmin(@Param("tenantId") Long tenantId);

    List<RoleVO> selectUserManageableRoleTree(RoleVO params);

    int queryUserManageableSonRoleList(@Param("roleId") Long roleId, @Param("userId") Long userId, @Param("levelPath") String levelPath);

    List<Role> listRole(@Param("tenantId") Long tenantId, @Param("userId") Long userId);
}

