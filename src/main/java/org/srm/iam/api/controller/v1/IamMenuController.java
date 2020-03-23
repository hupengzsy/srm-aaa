package org.srm.iam.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.srm.iam.api.dto.IamMenuDTO;
import org.srm.iam.app.service.IamMenuService;
import org.srm.iam.config.IamSwaggerApiConfig;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 16:00
 */
@Api(value = IamSwaggerApiConfig.MENU)
@RestController("iamMenuController.v1")
@RequestMapping("/v1/iam-menus")
public class IamMenuController extends BaseController {
    private final IamMenuService iamMenuService;

    @Autowired
    public IamMenuController(IamMenuService iamMenuService) {
        this.iamMenuService = iamMenuService;
    }

    @ApiOperation(value = "平台层查询SRM引用菜单列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<List<IamMenuDTO>> list(
                    @ApiParam(value = "菜单名称") @RequestParam(required = false) String menuName) {
        return Results.success(iamMenuService.listMenu(menuName));
    }
}
