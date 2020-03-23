package org.srm.iam.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.iam.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.iam.app.service.UserSynchronizeService;

/**
 * <p>
 * 子账户 API
 * </p>
 *
 * @author: shi.yan@hand-china.com 2020/2/14
 */
@RestController("userSynchronizeController.v1")
@RequestMapping("/v1")
public class UserSynchronizeController extends BaseController{

    @Autowired
    private UserSynchronizeService userSynchronizeService;

    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiOperation("子账户同步")
    @PostMapping({"/{organizationId}/user/synchronize"})
    public ResponseEntity<User> createUser(@PathVariable Long organizationId, @RequestBody User user) {
        user.setOrganizationId(organizationId);
        this.validObject(user);
        return Results.success(userSynchronizeService.synchronizeUser(user));
    }

}
