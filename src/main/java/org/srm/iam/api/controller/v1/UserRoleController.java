package org.srm.iam.api.controller.v1;

import java.util.Map;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.srm.iam.app.service.UserRoleService;
import org.srm.iam.config.IamSwaggerApiConfig;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/19 15:13
 */
@Api(value = IamSwaggerApiConfig.USER_ROLE)
@RestController("userRoleController.v1")
@RequestMapping("/v1/user-roles")
public class UserRoleController extends BaseController {
    private final UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }


    @ApiOperation(value = "根据用户id查询当前用户的所有租户及角色")
    @Permission(level = ResourceLevel.SITE, permissionLogin = true)
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> list(@PathVariable Long userId) {
        return Results.success(userRoleService.listUserRolesByUserId(userId));
    }
}
