package org.srm.iam.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.srm.iam.api.dto.IamMenuDTO;
import org.srm.iam.domain.entity.Menu;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 13:59
 */
@Component
public interface IamMenuMapper extends BaseMapper<Menu> {
    /**
     * 查询当前租户的SRM引用菜单列表（包括平台菜单及租户二开菜单）
     * 
     * @param tenantId 当前租户id
     * @param menuName 菜单名称
     * @param customFlag 菜单类型
     * @return List<Menu>
     */
    List<Menu> queryMenuByTenant(@Param("tenantId") Long tenantId, @Param("menuName") String menuName,
                    @Param("customFlag") Integer customFlag);

    /**
     * 查询0租户菜单
     * 
     * @param menuName 菜单名称
     * @param customFlag 菜单类型
     * @return List<Menu>
     */
    List<Menu> queryMenuTenantZero(@Param("menuName") String menuName, @Param("customFlag") Integer customFlag);

    /**
     * 平台层查询SRM引用菜单列表
     * 
     * @param menuName 菜单名称
     * @return List<IamMenuDTO>
     */
    List<IamMenuDTO> queryMenu(@Param("menuName") String menuName);
}
