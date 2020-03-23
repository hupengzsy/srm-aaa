package org.srm.iam.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.iam.api.dto.UserRoleDTO;
import org.srm.iam.app.service.UserRoleService;
import org.srm.iam.domain.entity.User;
import org.srm.iam.domain.repository.UserRoleRepository;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;

/**
 * <p>
 * 服务实现
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/22 11:38
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Map<String, Object> listUserRolesByUserId(Long userId) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userOrganizationId = userDetails.getOrganizationId();
        User user = userRoleRepository.selectByPrimaryKey(userId);
        List<UserRoleDTO> data = userRoleRepository.listRoleAndTenantByUserId(userOrganizationId, userId);
        Map<String, Object> result = new HashMap<>(3);
        result.put("realName", user.getRealName());
        result.put("imageUrl", user.getImageUrl());
        result.put("data", data);
        return result;
    }
}
