package org.srm.iam.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.iam.domain.entity.UserPortalEs;

/**
 * 用户二级域名关联表资源库
 *
 * @author shi.yan@hand-china.com 2020-02-24 17:19:40
 */
public interface UserPortalEsRepository extends BaseRepository<UserPortalEs> {

    Long selectTenantIdByWebUrl(String webUrl);

}
