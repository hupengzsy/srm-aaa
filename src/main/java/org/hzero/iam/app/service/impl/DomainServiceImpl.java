package org.hzero.iam.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.redis.RedisHelper;
import org.hzero.iam.api.dto.DomainDTO;
import org.hzero.iam.app.service.DomainService;
import org.hzero.iam.domain.entity.Domain;
import org.hzero.iam.domain.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : peng.yu01@hand-china.com 2019/12/9 19:58
 */
@Service
public class DomainServiceImpl implements DomainService {
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private RedisHelper redisHelper;

    public DomainServiceImpl() {
    }

    @Override
    public Page<Domain> selectByOptions(PageRequest pageRequest, Domain domain) {
        return PageHelper.doPage(pageRequest, () -> {
            return this.domainRepository.selectByOptions(domain);
        });
    }

    @Override
    public Domain selectByDomainId(Long domainId) {
        return this.domainRepository.selectByDomainId(domainId);
    }

    @Override
    public int updateDomain(Domain domain) {
        domain.vaidateDomainUrl(this.domainRepository);
        this.domainRepository.updateOptional(domain, new String[]{"loginNameField", "domainUrl", "ssoTypeCode", "ssoServerUrl", "ssoLoginUrl", "ssoLogoutUrl", "ssoClientId", "ssoClientPwd", "ssoUserInfo", "samlMetaUrl", "clientHostUrl", "remark"});
        DomainDTO redisDomain = new DomainDTO();
        redisDomain.setLoginNameField(domain.getLoginNameField());
        redisDomain.setDomainId(domain.getDomainId());
        redisDomain.setTenantId(domain.getTenantId());
        redisDomain.setCompanyId(domain.getCompanyId());
        redisDomain.setDomainUrl(domain.getDomainUrl());
        redisDomain.setSsoTypeCode(domain.getSsoTypeCode());
        redisDomain.setSsoServerUrl(domain.getSsoServerUrl());
        redisDomain.setSsoLoginUrl(domain.getSsoLoginUrl());
        redisDomain.setSsoLogoutUrl(domain.getSsoLogoutUrl());
        redisDomain.setSsoClientId(domain.getSsoClientId());
        redisDomain.setSsoClientPwd(domain.getSsoClientPwd());
        redisDomain.setSsoUserInfo(domain.getSsoUserInfo());
        redisDomain.setSamlMetaUrl(domain.getSamlMetaUrl());
        redisDomain.setClientHostUrl(domain.getClientHostUrl());
        Map<String, String> domainMap = new HashMap(1);
        domainMap.put(redisDomain.getDomainId().toString(), this.redisHelper.toJson(redisDomain));
        this.redisHelper.hshPutAll("hiam:domain", domainMap);
        return 0;
    }

    @Override
    public int insertDomain(Domain domain) {
        domain.vaidateDomainUrl(this.domainRepository);
        this.domainRepository.insertSelective(domain);
        DomainDTO redisDomain = new DomainDTO();
        redisDomain.setLoginNameField(domain.getLoginNameField());
        redisDomain.setDomainId(domain.getDomainId());
        redisDomain.setTenantId(domain.getTenantId());
        redisDomain.setCompanyId(domain.getCompanyId());
        redisDomain.setDomainUrl(domain.getDomainUrl());
        redisDomain.setSsoTypeCode(domain.getSsoTypeCode());
        redisDomain.setSsoServerUrl(domain.getSsoServerUrl());
        redisDomain.setSsoLoginUrl(domain.getSsoLoginUrl());
        redisDomain.setSsoLogoutUrl(domain.getSsoLogoutUrl());
        redisDomain.setSsoClientId(domain.getSsoClientId());
        redisDomain.setSsoClientPwd(domain.getSsoClientPwd());
        redisDomain.setSsoUserInfo(domain.getSsoUserInfo());
        redisDomain.setSamlMetaUrl(domain.getSamlMetaUrl());
        redisDomain.setClientHostUrl(domain.getClientHostUrl());
        Map<String, String> domainMap = new HashMap(1);
        domainMap.put(domain.getDomainId().toString(), this.redisHelper.toJson(redisDomain));
        this.redisHelper.hshPutAll("hiam:domain", domainMap);
        return 0;
    }

    @Override
    public int deleteDomain(Domain domain) {
        this.domainRepository.deleteByPrimaryKey(domain);
        this.redisHelper.hshDelete("hiam:domain", new Object[]{domain.getDomainId().toString()});
        return 0;
    }
}

