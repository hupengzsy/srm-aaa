package org.srm.iam.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.iam.api.dto.UserAuthorityDTO;
import org.hzero.iam.domain.entity.UserAuthority;
import org.hzero.iam.domain.entity.UserAuthorityLine;
import org.hzero.iam.domain.repository.UserAuthorityLineRepository;
import org.hzero.iam.domain.repository.UserAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.iam.app.service.UserAuthorityWrapperService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 标准用户权限api修改ServiceImpl
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/8/26 11:17
 */
@Service
public class UserAuthorityWrapperServiceImpl implements UserAuthorityWrapperService {
    private final UserAuthorityLineRepository userAuthorityLineRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Autowired
    public UserAuthorityWrapperServiceImpl(UserAuthorityLineRepository userAuthorityLineRepository,
                    UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityLineRepository = userAuthorityLineRepository;
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public List<UserAuthorityDTO> batchCreateUserAuthority(List<UserAuthorityDTO> userAuthorityDTOS) {
        List<UserAuthorityDTO> result = new ArrayList<>();

        for (UserAuthorityDTO userAuthorityDTO : userAuthorityDTOS) {
            result.add(this.createUserAuthority(userAuthorityDTO.getUserAuthority().getTenantId(),
                            userAuthorityDTO.getUserAuthority().getUserId(),
                            userAuthorityDTO.getUserAuthority().getAuthorityTypeCode(), userAuthorityDTO));
        }

        return result;
    }

    /**
     * 创建权限行
     * 
     * @param tenantId 租户id
     * @param userId 用户id
     * @param authorityTypeCode 权限类型编码
     * @param userAuthorityDTO UserAuthorityDTO
     * @return 回写之后的UserAuthorityDTO
     */
    @Transactional(rollbackFor = {Exception.class})
    public UserAuthorityDTO createUserAuthority(Long tenantId, Long userId, String authorityTypeCode,
                    UserAuthorityDTO userAuthorityDTO) {
        UserAuthority userAuthority = userAuthorityDTO.getUserAuthority();
        userAuthority.setTenantId(tenantId);
        userAuthority.setUserId(userId);
        userAuthority.setAuthorityTypeCode(authorityTypeCode);
        if (userAuthority.getAuthorityId() != null) {
            this.userAuthorityRepository.updateOptional(userAuthority, "includeAllFlag");
        } else if (this.userAuthorityRepository.insert(userAuthority) != 1) {
            throw new CommonException("error.error");
        }

        List<UserAuthorityLine> userAuthorityLineList = userAuthorityDTO.getUserAuthorityLineList();
        if (CollectionUtils.isNotEmpty(userAuthorityLineList)) {

            for (int i = 0; i < userAuthorityLineList.size(); i++) {
                UserAuthorityLine userAuthorityLine = userAuthorityLineList.get(i);
                userAuthorityLine.setTenantId(tenantId);
                userAuthorityLine.setAuthorityId(userAuthority.getAuthorityId());
                UserAuthorityLine exist = this.checkUserAuthorityLineRepeat(userAuthorityLine);
                if (!Objects.isNull(exist)) {
                    userAuthorityLineList.set(i, exist);
                } else {
                    this.userAuthorityLineRepository.insert(userAuthorityLine);
                }
            }
        }

        return userAuthorityDTO;
    }

    /**
     * 检查权限行是否已存在
     * 
     * @param userAuthorityLine 权限行
     * @return 存在则返回查到的权限行，否则返回null
     */
    private UserAuthorityLine checkUserAuthorityLineRepeat(UserAuthorityLine userAuthorityLine) {
        UserAuthorityLine temp = new UserAuthorityLine();
        temp.setAuthorityId(userAuthorityLine.getAuthorityId());
        temp.setDataId(userAuthorityLine.getDataId());
        return this.userAuthorityLineRepository.selectOne(temp);
    }
}
