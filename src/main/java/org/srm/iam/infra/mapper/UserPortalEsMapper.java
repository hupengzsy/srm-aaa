package org.srm.iam.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.iam.domain.entity.UserPortalEs;
import io.choerodon.mybatis.common.BaseMapper;

/**
 * 用户二级域名关联表Mapper
 *
 * @author shi.yan@hand-china.com 2020-02-24 17:19:40
 */
public interface UserPortalEsMapper extends BaseMapper<UserPortalEs> {

    Long selectTenantIdByWebUrl(@Param("webUrl") String webUrl);


}
