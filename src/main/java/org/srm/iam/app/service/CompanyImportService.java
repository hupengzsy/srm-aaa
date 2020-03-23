package org.srm.iam.app.service;

import org.hzero.iam.domain.entity.User;
import org.srm.iam.api.dto.UserDTO;
import org.srm.iam.domain.entity.UserEs;

import java.util.List;
import java.util.Map;

/**
 * 公司导入service
 *
 * @author peng.yang03@hand-china.com 2019/10/29 11:34
 */
public interface CompanyImportService {

    /**
     * 创建租户和用户
     *
     * @param sagaKey sagaKey
     * @param user    租户和用户信息
     * @return 注册好的用户
     */
    User registerUser(String sagaKey, User user);

    /**
     * 批量导入子账户信息
     *
     * @param users 子账户信息
     * @return 导入结果
     */
    Map<String, List<UserDTO>> batchCreateUserAccount(List<UserDTO> users);

    /**
     * 创建供应商子账户
     *
     * @param user       子账户
     * @param userEs     待更新的userEs
     * @param insertFlag 是否新建
     */
    void createUserAccount(UserDTO user, UserEs userEs, Integer insertFlag);

}
