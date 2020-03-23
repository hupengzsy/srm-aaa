package org.srm.iam.infra.feign.fallback;

import org.hzero.iam.domain.entity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.srm.iam.domain.vo.SslmCompanyVO;
import org.srm.iam.infra.feign.HpfmRemoteService;

import java.util.List;

/**
 * <p>
 * 国家服务远程调用接口
 * </p>
 *
 * @author qingsheng.chen 2018/7/3 星期二 9:40
 */
@Component
@SuppressWarnings("all")
public class HpfmRemoteServiceFallbackImpl implements HpfmRemoteService {

    private final Logger logger = LoggerFactory.getLogger(HpfmRemoteServiceFallbackImpl.class);

    @Override
    public Group insertOrUpdateGroup(Long tenantId, Group group) {
        logger.error("Create group failed with group {}.", group);
        return null;
    }

    @Override
    public SslmCompanyVO selectCompanyByNum(Long tenantId, String companyNumber) {
        logger.error("Get Company failed with companyNumber {}.", companyNumber);
        return null;
    }

    @Override
    public SslmCompanyVO insertOrUpdateCompany(Long tenantId, SslmCompanyVO company) {
        logger.error("Get Company failed with company {}.", company);
        return null;
    }

    @Override
    public List<SslmCompanyVO> selectTenantCompnayInfo(@PathVariable("organizationId") Long tenantId, @RequestParam(value = "companyName", required = false) String companyName) {
        logger.error("Get Company failed with company tenantId {} and companyName {}.", tenantId, companyName);
        return null;
    }

    @Override
    public List<Group> selectGroupsByTenantId(Long tenantId) {
        logger.error("Get groups failed with tenantId {}.", tenantId);
        return null;
    }
}
