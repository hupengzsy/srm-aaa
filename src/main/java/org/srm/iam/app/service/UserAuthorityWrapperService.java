package org.srm.iam.app.service;

import org.hzero.iam.api.dto.UserAuthorityDTO;

import java.util.List;

/**
 * <p>
 * 标准用户权限api修改Service
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/8/26 11:14
 */
public interface UserAuthorityWrapperService {
    /**
     * 批量创建用户权限，增加已有权限校验，用于不同的子公司与同一家供应商建立合作关系
     * 
     * @param userAuthorityDTOS 用户权限DTOs
     * @return 回写之后的List<UserAuthorityDTO>
     */
    List<UserAuthorityDTO> batchCreateUserAuthority(List<UserAuthorityDTO> userAuthorityDTOS);
}
