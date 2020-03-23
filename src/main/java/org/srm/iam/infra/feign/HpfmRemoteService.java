package org.srm.iam.infra.feign;

import org.hzero.common.HZeroService;
import org.hzero.iam.domain.entity.Group;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.srm.iam.domain.vo.SslmCompanyVO;
import org.srm.iam.infra.feign.fallback.HpfmRemoteServiceFallbackImpl;

import java.util.List;

/**
 * <p>
 * HMDM 远程服务
 * </p>
 *
 * @author qingsheng.chen 2018/7/3 星期二 9:24
 */
@FeignClient(value = HZeroService.Platform.NAME, fallback = HpfmRemoteServiceFallbackImpl.class, path = "v1/")
public interface HpfmRemoteService {

    /**
     * 创建/修改集团信息
     *
     * @param tenantId 租户ID
     * @param group    集团信息
     * @return 集团
     */
    @RequestMapping(method = RequestMethod.POST, path = "/{organizationId}/groups")
    public Group insertOrUpdateGroup(@PathVariable("organizationId") Long tenantId, @RequestBody Group group);

    /**
     * 根据编码查询公司
     *
     * @param tenantId      租户ID
     * @param companyNumber 公司编号
     * @return 公司信息
     */
    @GetMapping("/{organizationId}/companies/by-number/{companyNumber}")
    public SslmCompanyVO selectCompanyByNum(@PathVariable("organizationId") Long tenantId, @PathVariable("companyNumber") String companyNumber);

    /**
     * 创建/修改公司信息
     *
     * @param tenantId 租户id
     * @param company 公司信息
     * @return 公司信息
     */
    @PostMapping("/{organizationId}/companies")
    public SslmCompanyVO insertOrUpdateCompany(@PathVariable("organizationId") Long tenantId, @RequestBody SslmCompanyVO company);

    /**
     * 条件查询租户的公司信息
     *
     * @param tenantId    租户ID
     * @param companyName 公司名称(非必输)
     * @return 查出的公司列表
     */
    @GetMapping("/{organizationId}/companies")
    List<SslmCompanyVO> selectTenantCompnayInfo(@PathVariable("organizationId") Long tenantId, @RequestParam(value = "companyName", required = false) String companyName);

    /**
     * 列出语言列表
     *
     * @return 集团
     */
    @GetMapping("{organizationId}/groups/self")
    List<Group> selectGroupsByTenantId(@PathVariable("organizationId") Long tenantId);

}
