package org.srm.iam.app.service;

import java.util.Map;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/22 11:35
 */
public interface UserRoleService {
    /**
     * 根据用户id查询当前用户所有租户及角色
     *
     * @param userId 用户id
     * @return Map
     */
    Map<String, Object> listUserRolesByUserId(Long userId);
}
