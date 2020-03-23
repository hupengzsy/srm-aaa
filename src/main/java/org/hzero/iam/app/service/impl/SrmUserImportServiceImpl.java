package org.hzero.iam.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.AopProxy;
import org.hzero.core.base.BaseConstants;
import org.hzero.iam.api.dto.CompanyOuInvorgDTO;
import org.hzero.iam.api.dto.ResponseCompanyOuInvorgDTO;
import org.hzero.iam.app.service.SrmUserImportService;
import org.hzero.iam.app.service.UserAuthorityService;
import org.hzero.iam.domain.entity.UserAuthImport;
import org.hzero.iam.domain.entity.UserAuthority;
import org.hzero.iam.domain.entity.UserAuthorityLine;
import org.hzero.iam.domain.repository.UserAuthorityLineRepository;
import org.hzero.iam.domain.repository.UserAuthorityRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.srm.iam.infra.constant.IamConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * SRM 子账户权限导入实现
 * </p>
 *
 * @author: shi.yan@hand-china.com 2020/2/20
 */
@Service
public class SrmUserImportServiceImpl implements SrmUserImportService, AopProxy<SrmUserImportServiceImpl> {

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    @Autowired
    private UserAuthorityLineRepository userAuthorityLineRepository;
    @Autowired
    private UserAuthorityService userAuthorityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CompanyOuInvorgDTO> companyOuInvorgImport(Long tenantId, Long userId, List<CompanyOuInvorgDTO> companyOuInvorgDTOList) {

        Map<String, List<CompanyOuInvorgDTO>> collect = companyOuInvorgDTOList.stream().collect(Collectors.groupingBy(CompanyOuInvorgDTO::getTypeCode));
        List<CompanyOuInvorgDTO> companyList = collect.get(IamConstants.OrgTypeCode.COMPANY);
        List<CompanyOuInvorgDTO> ouList = collect.get(IamConstants.OrgTypeCode.OU);
        List<CompanyOuInvorgDTO> invorgList = collect.get(IamConstants.OrgTypeCode.INVORG);
        List<UserAuthorityLine> userAuthorityLineList = new ArrayList<>();

        UserAuthority userAuthority;

        if (CollectionUtils.isNotEmpty(companyList)) {
            userAuthority = new UserAuthority();
            final Long companyAuthorityId = userAuthority.getAuthorityIdByTenantIdAndUserId(tenantId, userId, IamConstants.OrgTypeCode.COMPANY, userAuthorityRepository);
            // 已经有的此类权限
            List<UserAuthorityLine> companyInDbList = userAuthorityLineRepository.selectByAuthorityIdAndTenantId(companyAuthorityId, tenantId);
            if (CollectionUtils.isEmpty(companyInDbList)) {
                companyList.forEach(item -> userAuthorityLineList.add(new UserAuthorityLine(companyAuthorityId, tenantId, item.getDataId(), item.getDataCode(), item.getDataName())));
            } else {
                Map<Long, UserAuthorityLine> companyInDbMap = companyInDbList.stream()
                        .collect(Collectors.toMap(UserAuthorityLine::getDataId, Function.identity()));
                companyList.forEach(item -> {
                    if (!companyInDbMap.containsKey(item.getDataId())) {
                        userAuthorityLineList.add(new UserAuthorityLine(companyAuthorityId, tenantId, item.getDataId(), item.getDataCode(), item.getDataName()));
                    }
                });
            }
        }

        if (CollectionUtils.isNotEmpty(ouList)) {
            userAuthority = new UserAuthority();
            final Long ouAuthorityId = userAuthority.getAuthorityIdByTenantIdAndUserId(tenantId, userId, IamConstants.OrgTypeCode.OU, userAuthorityRepository);
            // 已经有的此类权限
            List<UserAuthorityLine> ouInDbList = userAuthorityLineRepository.selectByAuthorityIdAndTenantId(ouAuthorityId, tenantId);
            if (CollectionUtils.isEmpty(ouInDbList)) {
                ouList.forEach(item -> userAuthorityLineList.add(new UserAuthorityLine(ouAuthorityId, tenantId, item.getDataId(), item.getDataCode(), item.getDataName())));
            } else {
                Map<Long, UserAuthorityLine> ouInDbMap = ouInDbList.stream()
                        .collect(Collectors.toMap(UserAuthorityLine::getDataId, Function.identity()));
                ouList.forEach(item -> {
                    if (!ouInDbMap.containsKey(item.getDataId())) {
                        userAuthorityLineList.add(new UserAuthorityLine(ouAuthorityId, tenantId, item.getDataId(), item.getDataCode(), item.getDataName()));
                    }
                });
            }
        }

        if (CollectionUtils.isNotEmpty(invorgList)) {
            userAuthority = new UserAuthority();
            final Long invorgAuthorityId = userAuthority.getAuthorityIdByTenantIdAndUserId(tenantId, userId, IamConstants.OrgTypeCode.INVORG, userAuthorityRepository);
            List<UserAuthorityLine> invorgInDbList = userAuthorityLineRepository.selectByAuthorityIdAndTenantId(invorgAuthorityId, tenantId);
            if (CollectionUtils.isEmpty(invorgInDbList)) {
                invorgList.forEach(item -> userAuthorityLineList.add(new UserAuthorityLine(invorgAuthorityId, tenantId, item.getDataId(), item.getDataCode(), item.getDataName())));
            } else {
                Map<Long, UserAuthorityLine> invorgInDbMap = invorgInDbList.stream()
                        .collect(Collectors.toMap(UserAuthorityLine::getDataId, Function.identity()));
                invorgList.forEach(item -> {
                    if (!invorgInDbMap.containsKey(item.getDataId())) {
                        userAuthorityLineList.add(new UserAuthorityLine(invorgAuthorityId, tenantId, item.getDataId(), item.getDataCode(), item.getDataName()));
                    }
                });
            }
        }
        this.userAuthorityLineRepository.batchInsert(userAuthorityLineList);
        return companyOuInvorgDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAuthority createOrUpdateUserAuthority(UserAuthImport userAuthImport, Integer includeAllFlag) {
        UserAuthority userAuthority = new UserAuthority();
        BeanUtils.copyProperties(userAuthImport, userAuthority);
        userAuthority.setIncludeAllFlag(includeAllFlag);
        UserAuthority queryExists = new UserAuthority();
        queryExists.setTenantId(userAuthImport.getTenantId());
        queryExists.setUserId(userAuthImport.getUserId());
        queryExists.setAuthorityTypeCode(userAuthImport.getAuthorityTypeCode());
        queryExists = this.userAuthorityRepository.selectOne(queryExists);
        if (queryExists == null) {
            userAuthorityRepository.insertSelective(userAuthority);
        } else if (BaseConstants.Flag.YES.equals(includeAllFlag)) {
            userAuthority.setAuthorityId(queryExists.getAuthorityId());
            userAuthority.setObjectVersionNumber(queryExists.getObjectVersionNumber());
            userAuthorityRepository.updateOptional(userAuthority, UserAuthority.FIELD_INCLUDE_ALL_FLAG);
        } else {
            userAuthority.setAuthorityId(queryExists.getAuthorityId());
        }
        return userAuthority;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void companyOuInvorgInsertOrUpdate(UserAuthImport userAuthImport) {
        ResponseCompanyOuInvorgDTO responseCompanyOuInvorgDTO = userAuthorityService.listComanyOuInvorg(userAuthImport.getTenantId(), userAuthImport.getUserId(), null, null);
        List<CompanyOuInvorgDTO> ouList = new ArrayList<>();
        List<CompanyOuInvorgDTO> invorgList = new ArrayList<>();
        List<CompanyOuInvorgDTO> writeToDb = new ArrayList<>();
        Assert.isTrue(CollectionUtils.isNotEmpty(responseCompanyOuInvorgDTO.getOriginList()), IamConstants.ErrorCode.USER_AUTHORITY_NOT_SELECT);
        if (userAuthImport.getAuthorityTypeCode().equals(IamConstants.OrgTypeCode.COMPANY)) {
            CompanyOuInvorgDTO company = responseCompanyOuInvorgDTO.getOriginList().stream()
                    .filter(companyOuInvorgDTO -> companyOuInvorgDTO.getDataCode().equals(userAuthImport.getDataCode())
                            && companyOuInvorgDTO.getDataName().equals(userAuthImport.getDataName())
                            && companyOuInvorgDTO.getTypeCode().equals(IamConstants.OrgTypeCode.COMPANY))
                    .findFirst().orElse(null);
            Assert.notNull(company, "error.data_exists");
            if (CollectionUtils.isNotEmpty(company.getChildren())) {
                ouList = company.getChildren();
            }
            for (CompanyOuInvorgDTO companyOuInvorgDTO : ouList) {
                if (CollectionUtils.isNotEmpty(companyOuInvorgDTO.getChildren())) {
                    invorgList.addAll(companyOuInvorgDTO.getChildren());
                }
            }
            writeToDb.add(company);
            writeToDb.addAll(ouList);
            writeToDb.addAll(invorgList);
        }
        // 业务组织
        if (userAuthImport.getAuthorityTypeCode().equals(IamConstants.OrgTypeCode.OU)) {
            CompanyOuInvorgDTO ou = responseCompanyOuInvorgDTO.getOriginList().stream()
                    .filter(companyOuInvorgDTO -> companyOuInvorgDTO.getDataCode().equals(userAuthImport.getDataCode())
                            && companyOuInvorgDTO.getDataName().equals(userAuthImport.getDataName())
                            && companyOuInvorgDTO.getTypeCode().equals(IamConstants.OrgTypeCode.OU))
                    .findFirst().orElse(null);
            // 获取业务单元下面的库存组织
            Assert.notNull(ou, "error.data_exists");
            if (CollectionUtils.isNotEmpty(ou.getChildren())) {
                invorgList.addAll(ou.getChildren());
            }
            writeToDb.add(ou);
            writeToDb.addAll(invorgList);
        }
        // 库存组织
        if (userAuthImport.getAuthorityTypeCode().equals(IamConstants.OrgTypeCode.INV_ORGANIZATION)) {
            CompanyOuInvorgDTO invorg = responseCompanyOuInvorgDTO.getOriginList().stream()
                    .filter(companyOuInvorgDTO -> companyOuInvorgDTO.getDataCode().equals(userAuthImport.getDataCode())
                            && companyOuInvorgDTO.getDataName().equals(userAuthImport.getDataName())
                            && companyOuInvorgDTO.getTypeCode().equals(IamConstants.OrgTypeCode.INVORG))
                    .findFirst().orElse(null);

            // 获取业务单元下面的库存组织
            Assert.notNull(invorg, "error.data_exists");
            writeToDb.add(invorg);
        }
        self().companyOuInvorgImport(userAuthImport.getTenantId(), userAuthImport.getUserId(), writeToDb);
    }
}
