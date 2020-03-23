package org.srm.iam.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.srm.iam.domain.entity.Role;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 10:58
 */
@Component
public interface TenantRoleMapper extends BaseMapper<Role> {
    /**
     * 根据租户id查询角色
     * @param organizationId 租户id
     * @return 角色列表
     */
    List<Role> selectRoleByOrganizationId(@Param("organizationId") Long organizationId);
}
