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
import org.hzero.core.base.Result;
import org.hzero.core.util.Results;
import org.hzero.iam.api.dto.UserAuthorityDataDTO;
import org.hzero.iam.domain.repository.UserAuthorityLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * description
 * </p>
 *
 * @author haoran.li01@hand-china.com 2019/11/05 19:00
 */
@Api(
        tags = {"Srm User Authority"}
)
@RestController("SrmUserAuthorityController.v1")
@RequestMapping("/v1/{organizationId}/users/{userId}")
public class SrmUserAuthorityController extends BaseController {

    @Autowired
    UserAuthorityLineRepository userAuthorityLineRepository;

    @ApiOperation("用户层分页查询职位")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/positions"})
    @CustomPageRequest
    public ResponseEntity<Page<UserAuthorityDataDTO>> listPosition(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "用户ID",required = true) @PathVariable("userId") Long userId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.userAuthorityLineRepository.listPosition( organizationId,  userId,  dataCode, dataName, pageRequest));
    }

    @ApiOperation("用户层分页查询组织")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/groups"})
    @CustomPageRequest
    public ResponseEntity<Page<UserAuthorityDataDTO>> listGroup(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "用户ID",required = true) @PathVariable("userId") Long userId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.userAuthorityLineRepository.listGroup( organizationId,  userId,  dataCode, dataName, pageRequest));
    }

    @ApiOperation("用户层分页查询员工")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/employees"})
    @CustomPageRequest
    public ResponseEntity<Page<UserAuthorityDataDTO>> listEmployee(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "用户ID",required = true) @PathVariable("userId") Long userId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.userAuthorityLineRepository.listEmployee( organizationId,  userId,  dataCode, dataName, pageRequest));

    }

    @ApiOperation("用户层分页查询部门")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/units"})
    @CustomPageRequest
    public ResponseEntity<Page<UserAuthorityDataDTO>> listUnit(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "用户ID",required = true) @PathVariable("userId") Long userId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.userAuthorityLineRepository.listUnit( organizationId,  userId,  dataCode, dataName, pageRequest));
    }

    @ApiOperation("用户层分页查询采购物料")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/data/purchase-item"})
    @CustomPageRequest
    public ResponseEntity<Page<UserAuthorityDataDTO>> listPurchaseItem(@ApiParam(value = "租户ID",required = true) @PathVariable("organizationId") Long organizationId, @ApiParam(value = "用户ID",required = true) @PathVariable("userId") Long userId, @ApiParam("编码") @RequestParam(value = "dataCode",required = false) String dataCode, @ApiParam("名称") @RequestParam(value = "dataName",required = false) String dataName, @ApiIgnore @SortDefault(value = {"dataId"},direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.userAuthorityLineRepository.listPurchaseItem( organizationId,  userId,  dataCode, dataName, pageRequest));
    }

}
