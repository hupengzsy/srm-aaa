package org.srm.iam.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.iam.domain.entity.UserPortalEs;
import org.srm.iam.domain.repository.UserPortalEsRepository;
import org.springframework.stereotype.Component;
import org.srm.iam.infra.mapper.UserPortalEsMapper;

/**
 * 用户二级域名关联表 资源库实现
 *
 * @author shi.yan@hand-china.com 2020-02-24 17:19:40
 */
@Component
public class UserPortalEsRepositoryImpl extends BaseRepositoryImpl<UserPortalEs> implements UserPortalEsRepository {

    @Autowired
    private UserPortalEsMapper userPortalEsMapper;

    @Override
    public Long selectTenantIdByWebUrl(String webUrl) {
        return userPortalEsMapper.selectTenantIdByWebUrl(webUrl);
    }
}
