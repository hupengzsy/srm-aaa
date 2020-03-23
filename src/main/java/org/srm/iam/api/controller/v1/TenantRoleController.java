package org.srm.iam.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.srm.iam.app.service.TenantRoleService;
import org.srm.iam.config.IamSwaggerApiConfig;
import org.srm.iam.domain.entity.Role;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 10:04
 */
@Api(value = IamSwaggerApiConfig.TENANT_ROLE)
@RestController("tenantRoleController.v1")
@RequestMapping("/v1/{organizationId}/tenant-roles")
public class TenantRoleController extends BaseController {
    private final TenantRoleService tenantRoleService;

    @Autowired
    public TenantRoleController(TenantRoleService tenantRoleService) {
        this.tenantRoleService = tenantRoleService;
    }

    @ApiOperation(value = "查询当前租户的所有角色")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<List<Role>> list(@PathVariable Long organizationId) {
        return Results.success(tenantRoleService.listRoleByOrganizationId(organizationId));
    }
}
