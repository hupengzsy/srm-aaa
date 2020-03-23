package org.srm.iam.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.iam.api.controller.v1.UserAuthorityController;
import org.hzero.iam.api.dto.UserAuthorityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.iam.app.service.UserAuthorityWrapperService;
import org.srm.iam.config.IamSwaggerApiConfig;

import java.util.List;

/**
 * <p>
 * 用户层批量保存用户权限数据，对原有的api进行修改，用于多个子公司与同一家供应商建立合作关系，其他业务请使用标准api
 * 
 * @see UserAuthorityController#batchCreate(java.lang.Long, java.lang.Long, java.lang.String,
 *      java.util.List)
 *      </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/8/26 11:10
 */
@Api(value = IamSwaggerApiConfig.USER_AUTHORITY_WRAPPER)
@RestController("userAuthorityWrapController.v1")
@RequestMapping("/v1/{organizationId}/users/{userId}")
public class UserAuthorityWrapperController extends BaseController {
    private final UserAuthorityWrapperService userAuthorityWrapperService;

    @Autowired
    public UserAuthorityWrapperController(UserAuthorityWrapperService userAuthorityWrapperService) {
        this.userAuthorityWrapperService = userAuthorityWrapperService;
    }

    @ApiOperation("用户层批量保存用户数据权限，用于多个子公司与同一家供应商建立合作关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping({"/authority/batch-save-wrap"})
    public ResponseEntity<List<UserAuthorityDTO>> batchSaveWrap(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long organizationId,
                    @ApiParam(value = "用户ID", required = true) @PathVariable("userId") Long userId,
                    @ApiParam(value = "类型",
                                    required = true) @RequestParam("authorityTypeCode") String authorityTypeCode,
                    @ApiParam(value = "用户数据权限头行",
                                    required = true) @RequestBody List<UserAuthorityDTO> userAuthorityDTOList) {
        if (CollectionUtils.isNotEmpty(userAuthorityDTOList)) {
            userAuthorityDTOList.forEach(
                            userAuthorityDTO -> userAuthorityDTO.getUserAuthority().setTenantId(organizationId));
        }

        return Results.success(this.userAuthorityWrapperService.batchCreateUserAuthority(userAuthorityDTOList));
    }
}
