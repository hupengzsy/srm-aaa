package org.srm.iam.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.iam.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.iam.api.dto.UserDTO;
import org.srm.iam.app.service.CompanyImportService;
import org.srm.iam.config.IamSwaggerApiConfig;
import org.srm.iam.infra.constant.IamConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 批量导入公司信息
 *
 * @author peng.yang03@hand-china.com 2019/10/29 11:32
 */
@Api(value = IamSwaggerApiConfig.COMPANY_IMPORT)
@RestController("companyImportController.v1")
@RequestMapping("/v1/company-import")
public class CompanyImportController extends BaseController {
    @Autowired
    private CompanyImportService supplierImportService;

    @PostMapping("/user")
    @ApiOperation(value = "注册用户")
    @Permission(level = ResourceLevel.SITE, permissionLogin = true)
    public User registerUser(@RequestParam("sagaKey") String sagaKey, @RequestBody User user) {
        return supplierImportService.registerUser(sagaKey, user);
    }

    @PostMapping("/user-account")
    @ApiOperation(value = "供应商子账户导入")
    @Permission(level = ResourceLevel.SITE, permissionLogin = true)
    public ResponseEntity<Map<String, List<UserDTO>>> batchCreateUserAccount(@RequestBody List<UserDTO> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Results.success();
        }
        List<UserDTO> validUsers = new ArrayList<>();
        List<UserDTO> invalidUsers = new ArrayList<>();
        users.forEach(user -> {
            try {
                this.validObject(user);
                validUsers.add(user);
            } catch (Exception e) {
                user.setRegisteredMessage(e.getMessage());
                invalidUsers.add(user);
            }
        });
        Map<String, List<UserDTO>> result = supplierImportService.batchCreateUserAccount(validUsers);
        if (result.get(IamConstants.FAILURE_STATUS) == null) {
            result.put(IamConstants.FAILURE_STATUS, invalidUsers);
        } else {
            result.get(IamConstants.FAILURE_STATUS).addAll(invalidUsers);
        }
        return Results.success(result);
    }
}
