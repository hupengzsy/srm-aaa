package org.hzero.iam.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IDoImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.core.base.BaseConstants;
import org.hzero.iam.api.dto.CompanyOuInvorgDTO;
import org.hzero.iam.api.dto.ResponseCompanyOuInvorgDTO;
import org.hzero.iam.app.service.SrmUserImportService;
import org.hzero.iam.app.service.UserAuthorityService;
import org.hzero.iam.domain.entity.UserAuthImport;
import org.hzero.iam.domain.entity.UserAuthority;
import org.hzero.iam.domain.entity.UserAuthorityLine;
import org.hzero.iam.domain.repository.TenantRepository;
import org.hzero.iam.domain.repository.UserAuthorityLineRepository;
import org.hzero.iam.domain.repository.UserAuthorityRepository;
import org.hzero.iam.domain.repository.UserRepository;
import org.hzero.iam.infra.mapper.UserAuthorityMapperLineData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.srm.iam.infra.constant.IamConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 子账户导入 权限导入
 * </p>
 *
 * @author: shi.yan@hand-china.com 2020/2/17
 */
@ImportService(templateCode = "HIAM.AUTH_CREATE")
public class AuthImportServiceImpl implements IDoImportService {

    private final Long INCLUDE_ALL_FLAG = 1L;
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    @Autowired
    private UserAuthorityLineRepository userAuthorityLineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserAuthorityService userAuthorityService;
    @Autowired
    private UserAuthorityMapperLineData userAuthorityMapperLineData;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SrmUserImportService srmUserImportService;


    public AuthImportServiceImpl() {
    }

    public Boolean doImport(String data) {
        UserAuthImport userAuthImport;
        try {
            userAuthImport = this.objectMapper.readValue(data, UserAuthImport.class);
        } catch (IOException ex) {
            throw new CommonException("error.data_invalid");
        }
        userAuthImport.validParam(this.userRepository, this.tenantRepository);
        ResponseCompanyOuInvorgDTO responseCompanyOuInvorgDTO = new ResponseCompanyOuInvorgDTO();
        // 加入全部
        if (userAuthImport.getIncludeAllFlag() != null && userAuthImport.getIncludeAllFlag().equals(INCLUDE_ALL_FLAG)) {
            switch (userAuthImport.getAuthorityTypeCode()) {
                case IamConstants.OrgTypeCode.COMPANY:
                    responseCompanyOuInvorgDTO = userAuthorityService.listComanyOuInvorg(userAuthImport.getTenantId(), userAuthImport.getUserId(), null, null);
                    if (CollectionUtils.isNotEmpty(responseCompanyOuInvorgDTO.getOriginList())) {
                        srmUserImportService.companyOuInvorgImport(userAuthImport.getTenantId(), userAuthImport.getUserId(), responseCompanyOuInvorgDTO.getOriginList());
                    }
                    return true;
                case IamConstants.OrgTypeCode.OU:
                    responseCompanyOuInvorgDTO = userAuthorityService.listComanyOuInvorg(userAuthImport.getTenantId(), userAuthImport.getUserId(), null, null);
                    if (CollectionUtils.isNotEmpty(responseCompanyOuInvorgDTO.getOriginList())) {
                        List<CompanyOuInvorgDTO> ou = responseCompanyOuInvorgDTO.getOriginList()
                                .stream()
                                .filter(item -> !item.getTypeCode().equals(IamConstants.OrgTypeCode.COMPANY))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(ou)) {
                            srmUserImportService.companyOuInvorgImport(userAuthImport.getTenantId(), userAuthImport.getUserId(), ou);
                        }
                    }
                    return true;

                case IamConstants.OrgTypeCode.INV_ORGANIZATION:
                    responseCompanyOuInvorgDTO = userAuthorityService.listComanyOuInvorg(userAuthImport.getTenantId(), userAuthImport.getUserId(), null, null);
                    if (CollectionUtils.isNotEmpty(responseCompanyOuInvorgDTO.getOriginList())) {
                        List<CompanyOuInvorgDTO> invorg = responseCompanyOuInvorgDTO.getOriginList()
                                .stream()
                                .filter(item -> item.getTypeCode().equals(IamConstants.OrgTypeCode.INVORG))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(invorg)) {
                            srmUserImportService.companyOuInvorgImport(userAuthImport.getTenantId(), userAuthImport.getUserId(), invorg);
                        }
                    }
                    return true;
                default:
                    srmUserImportService.createOrUpdateUserAuthority(userAuthImport, BaseConstants.Flag.YES);
                    return true;

            }
            // 单独权限加入
        } else {
            switch (userAuthImport.getAuthorityTypeCode()) {
                case IamConstants.OrgTypeCode.COMPANY:
                case IamConstants.OrgTypeCode.OU:
                case IamConstants.OrgTypeCode.INV_ORGANIZATION:
                    srmUserImportService.companyOuInvorgInsertOrUpdate(userAuthImport);
                    return true;
                // 客户
                case IamConstants.OrgTypeCode.CUSTOMER:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryCusDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 供应商
                case IamConstants.OrgTypeCode.SUPPLIER:
                    userAuthImport.setDataId(userAuthorityMapperLineData.querySupDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 采购组织
                case IamConstants.OrgTypeCode.PURORG:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryPOrgDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 采购员
                case IamConstants.OrgTypeCode.PURAGENT:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryPAgentDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 采购品类
                case IamConstants.OrgTypeCode.PURCAT:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryPCatDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 采购物料
                case IamConstants.OrgTypeCode.PURITEM:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryPItemDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 组织架构-公司
                case IamConstants.OrgTypeCode.GROUP:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryGroupDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 部门
                case IamConstants.OrgTypeCode.UNIT:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryUnitDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 岗位
                case IamConstants.OrgTypeCode.POSITION:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryPositionDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;
                // 员工
                case IamConstants.OrgTypeCode.EMPLOYEE:
                    userAuthImport.setDataId(userAuthorityMapperLineData.queryEmployeeDataSourceInfo(userAuthImport.getTenantId(), userAuthImport.getDataCode(), userAuthImport.getDataName()));
                    break;

            }
            UserAuthority userAuthority = srmUserImportService.createOrUpdateUserAuthority(userAuthImport, BaseConstants.Flag.NO);
            if (userAuthImport.getDataId() == null) {
                throw new CommonException("error.data_invalid");
            } else {
                UserAuthorityLine userAuthorityLine = new UserAuthorityLine();
                BeanUtils.copyProperties(userAuthImport, userAuthorityLine);
                userAuthorityLine.setAuthorityId(userAuthority.getAuthorityId());
                UserAuthorityLine uniqueIndex = new UserAuthorityLine();
                uniqueIndex.setAuthorityId(userAuthorityLine.getAuthorityId());
                uniqueIndex.setDataId(userAuthorityLine.getDataId());
                if (this.userAuthorityLineRepository.selectOne(uniqueIndex) != null) {
                    throw new CommonException("error.data_exists");
                } else {
                    this.userAuthorityLineRepository.insertSelective(userAuthorityLine);
                    return true;
                }
            }
        }
    }
}