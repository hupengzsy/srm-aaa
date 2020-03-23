package org.srm.iam.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.srm.iam.app.service.ApproveRegisterCompanyService;
import org.srm.iam.config.IamSwaggerApiConfig;
import org.srm.iam.domain.vo.SslmCompanyVO;

import java.util.Map;

/**
 * 企业注册审批
 *
 * @author peng.yang@hand-china.com 2019/07/30 17:39
 */
@Api(value = IamSwaggerApiConfig.APPROVE_COMPANY)
@RestController("approveCompanyController.v1")
@RequestMapping("/v1/approve-company")
public class ApproveRegisterCompanyController {

    @Autowired
    private ApproveRegisterCompanyService approveService;

    @PostMapping
    @ApiOperation(value = "企业注册自动审批")
    @Permission(level = ResourceLevel.SITE)
    public Map approveRegisterCompany(@RequestParam("sagaKey") String sagaKey, @RequestBody SslmCompanyVO companyVO) {
        return approveService.approveRegisterCompany(sagaKey, companyVO);
    }

    @PostMapping("/user-authority-role-assign")
    @ApiOperation(value = "用户和角色权限分配")
    @Permission(level = ResourceLevel.SITE)
    public void createTreeUserAuthorityAndAssignRole(@RequestParam("sagaKey") String sagaKey,
                    @RequestBody SslmCompanyVO companyVO) {
        approveService.createTreeUserAuthorityAndAssignRole(sagaKey, companyVO);
    }

}
