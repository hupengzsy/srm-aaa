package org.srm.iam.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.srm.iam.api.dto.IamMenuDTO;
import org.srm.iam.domain.entity.Menu;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 15:54
 */
public interface IamMenuService {
    /**
     * 查询当前租户的SRM引用菜单列表（包括平台菜单及租户二开菜单）
     *
     * @param siteMenuFlag 平台菜单标志，true查询0租户，false查询当前租户
     * @param tenantId 当前租户id
     * @param menuName 菜单名称
     * @param customFlag 菜单类型
     * @param pageRequest 分页参数
     * @return Page<Menu>
     */
    Page<Menu> listMenuByTenant(boolean siteMenuFlag, Long tenantId, String menuName, Integer customFlag,
                    PageRequest pageRequest);

    /**
     * 平台层查询SRM引用菜单列表,包含平台模块名称
     *
     * @param menuName 菜单名称
     * @return List<IamMenuDTO>
     */
    List<IamMenuDTO> listMenu(String menuName);
}
