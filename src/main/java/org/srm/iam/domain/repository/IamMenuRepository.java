package org.srm.iam.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.iam.api.dto.IamMenuDTO;
import org.srm.iam.domain.entity.Menu;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 14:01
 */
public interface IamMenuRepository extends BaseRepository<Menu> {

    /**
     * 查询当前租户的SRM引用菜单列表（包括平台菜单及租户二开菜单）
     * 
     * @param tenantId 当前租户id
     * @param menuName 菜单名称
     * @param customFlag 菜单类型
     * @return List<Menu>
     */
    List<Menu> listMenuByTenant(Long tenantId, String menuName, Integer customFlag);

    /**
     * 查询0租户菜单
     *
     * @param menuName 菜单名称
     * @param customFlag 菜单类型
     * @return List<Menu>
     */
    List<Menu> listMenuTenantZero(String menuName, Integer customFlag);

    /**
     * 平台层查询SRM引用菜单列表,包含平台模块名称
     * 
     * @param menuName 菜单名称
     * @return List<IamMenuDTO>
     */
    List<IamMenuDTO> listMenu(String menuName);
}
