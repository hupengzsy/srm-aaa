package org.hzero.iam.infra.repository.impl;

import java.util.List;

import org.hzero.iam.domain.entity.Domain;
import org.hzero.iam.domain.repository.DomainRepository;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.infra.mapper.DomainPlusMapper;

/**
 * @author peng.yu01@hand-china.com 2019-12-02
 */
@Component
public class DomainRepositoryImpl extends BaseRepositoryImpl<Domain> implements DomainRepository {
    @Autowired
    private DomainPlusMapper domainPlusMapper;

    @Override
    public List<Domain> selectByOptions(Domain domain) {
        return this.domainPlusMapper.selectByOptions(domain);
    }

    @Override
    public Domain selectByDomainId(Long domainId) {
        return this.domainPlusMapper.selectByDomainId(domainId);
    }
}

