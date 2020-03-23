package org.srm.iam.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.hzero.iam.domain.entity.Domain;

/**
 * @author peng.yu01@hand-china.com 2019-12-02
 */
public interface DomainPlusMapper {

    List<Domain> selectByOptions(Domain domain);

    Domain selectByDomainId(@Param("domainId") Long domainId);
}
