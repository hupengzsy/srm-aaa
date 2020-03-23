package org.srm.iam.api.controller.v1;


import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.iam.app.service.RoleAuthDataLineWrapperService;
import org.srm.iam.config.IamSwaggerApiConfig;
import org.srm.iam.domain.entity.SrmRoleAuthDataLine;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * description
 * </p>
 *
 * @author haoran.li01@hand-china.com 2019/09/17 13:00
 */

@Api(value = IamSwaggerApiConfig.ROLE_AUTH_DATA_WRAPPER)
@RestController("RoleAuthDataWrapperController.v1")
@RequestMapping({"/v1/{organizationId}/role/{roleId}"})
public class RoleAuthDataWrapperController extends BaseController {

    @Autowired
    RoleAuthDataLineWrapperService roleAuthDataLineWrapperService;

    @ApiOperation("角色层分页查询客户")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/customers"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pageCustomer(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pageCustomer(organizationId, roleId, dataCode, dataName, pageRequest));
    }

    @ApiOperation("角色层分页查询供应商")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/suppliers"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pageSupplier(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pageSupplier(organizationId, roleId, dataCode, dataName, pageRequest));
    }

    @ApiOperation("角色层分页查询采购品类")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/purchase-category"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pagePurchaseCategory(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pagePurchaseCategory(organizationId, roleId, dataCode, dataName, pageRequest));
    }

    @ApiOperation("角色层分页查询采购物料")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/purchase-item"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pagePurchaseItem(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pagePurchaseItem(organizationId, roleId, dataCode, dataName, pageRequest));
    }

    @ApiOperation("角色层分页查询销售产品")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/supplier-item"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pageSupplierItem(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pageSupplierItem(organizationId, roleId, dataCode, dataName, pageRequest));
    }

    @ApiOperation("角色层分页查询HR公司")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/group"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pageGroup(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pageGroup(organizationId, roleId, dataCode, dataName, pageRequest));
    }

    @ApiOperation("角色层分页查询部门")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/unit"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pageUnit(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pageUnit(organizationId, roleId, dataCode, dataName, pageRequest));
    }

    @ApiOperation("角色层分页查询岗位")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/position"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pagePosition(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pagePosition(organizationId, roleId, dataCode, dataName, pageRequest));
    }

    @ApiOperation("角色层分页查询员工")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/employee"})
    @CustomPageRequest
    public ResponseEntity<Page<SrmRoleAuthDataLine>> pageEmployee(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "角色ID",required = true) @PathVariable("roleId") Long roleId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.roleAuthDataLineWrapperService.pageEmployee(organizationId, roleId, dataCode, dataName, pageRequest));
    }


}
