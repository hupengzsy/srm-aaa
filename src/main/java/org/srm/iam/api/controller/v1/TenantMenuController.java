package org.srm.iam.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.iam.app.service.IamMenuService;
import org.srm.iam.config.IamSwaggerApiConfig;
import org.srm.iam.domain.entity.Menu;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 16:31
 */
@Api(value = IamSwaggerApiConfig.TENANT_MENU)
@RestController("tenantMenuController.v1")
@RequestMapping("/v1/{organizationId}/menus")
public class TenantMenuController extends BaseController {
    private final IamMenuService iamMenuService;

    @Autowired
    public TenantMenuController(IamMenuService iamMenuService) {
        this.iamMenuService = iamMenuService;
    }

    @ApiOperation(value = "查询当前租户的SRM引用菜单列表（包括平台菜单及租户二开菜单）")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<Menu>> list(@PathVariable Long organizationId,
                    @ApiParam(value = "菜单名称") @RequestParam(required = false) String menuName,
                    @ApiParam(value = "菜单类型") @RequestParam(required = false) Integer customFlag,
                    @ApiParam(value = "平台菜单标识:true 0租户，false 路径中租户") @RequestParam boolean siteMenuFlag,
                    @ApiIgnore @SortDefault(value = Menu.FIELD_ID,
                                    direction = Sort.Direction.ASC) PageRequest pageRequest) {
        // siteMenuFlag：true 查询0租户，false 查询url中租户
        return Results.success(iamMenuService.listMenuByTenant(siteMenuFlag, organizationId, menuName, customFlag,
                        pageRequest));
    }
}
