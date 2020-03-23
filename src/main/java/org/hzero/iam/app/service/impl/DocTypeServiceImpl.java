//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hzero.iam.app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.ObjectUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseConstants.Flag;
import org.hzero.core.message.MessageAccessor;
import org.hzero.core.util.Pair;
import org.hzero.core.util.ResponseUtils;
import org.hzero.iam.app.service.DocTypeAssignService;
import org.hzero.iam.app.service.DocTypeService;
import org.hzero.iam.domain.entity.DocType;
import org.hzero.iam.domain.entity.DocTypeAssign;
import org.hzero.iam.domain.entity.DocTypeAuthDim;
import org.hzero.iam.domain.entity.DocTypePermission;
import org.hzero.iam.domain.repository.DocTypePermissionRepository;
import org.hzero.iam.domain.repository.DocTypeRepository;
import org.hzero.iam.domain.vo.DataPermissionRangeVO;
import org.hzero.iam.domain.vo.DataPermissionRuleVO;
import org.hzero.iam.infra.feign.PermissionRuleFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.srm.iam.domain.entity.SrmLovValue;
import org.srm.iam.domain.repository.SrmLovValueRepository;


@Service
public class DocTypeServiceImpl implements DocTypeService {

    private static final String LOV_CODE = "HIAM.AUTHORITY_TYPE_CODE";

    private static final Logger logger = LoggerFactory.getLogger(DocTypeServiceImpl.class);
    private static final String DOC_TYPE_PERMISSION_MESSAGE = "doc.type.permission.description";
    private static final String DOC_TYPE_PERMISSION_CODE_RULE_PREFIX = "DT";
    private static final String DOC_TYPE_PERMISSION_REPLACE = "${authorityTypeCode}";
    private static final String DOC_TYPE_FIELD_REPLACE = "${fieldName}";
    private static final String DOC_TYPE_PERMISSION_RULE_ALL = "(EXISTS (SELECT 1 \n            FROM hiam_role_authority_line hral\n           WHERE hral.role_id IN (#{roleMergeIds})\n             AND hral.auth_type_code = '${authorityTypeCode}'\n             AND (EXISTS (SELECT 1\n                            FROM hiam_role_auth_data hrad\n                       LEFT JOIN hiam_role_auth_data_line hradl ON hrad.auth_data_id = hradl.auth_data_id\n                           WHERE hrad.role_id = hral.role_id\n                             AND hrad.tenant_id = #{tenantId}\n                             AND hrad.authority_type_code = hral.auth_type_code\n                             AND (hrad.include_all_flag = 1 OR hradl.data_id = #{tableAlias}.${fieldName})\n                             AND NOT EXISTS (SELECT 1\n                                               FROM hiam_user_authority hua\n                                              WHERE hua.tenant_id = hrad.tenant_id\n                                                AND hua.user_id = #{userId}\n                                                AND hua.authority_type_code = hral.auth_type_code))\n                  OR EXISTS (SELECT 1\n                               FROM hiam_user_authority hua1\n                          LEFT JOIN hiam_user_authority_line hual1 ON hua1.authority_id = hual1.authority_id\n                              WHERE hua1.tenant_id = #{tenantId}\n                                AND hua1.user_id = #{userId}\n                                AND hua1.authority_type_code = hral.auth_type_code\n                                AND (hua1.include_all_flag = 1 OR hual1.data_id = #{tableAlias}.${fieldName})))))";
    private static final String DOC_TYPE_PERMISSION_RULE_OC = "(EXISTS (SELECT 1 \n            FROM hiam_role_authority_line hral\n           WHERE hral.role_id IN (#{roleMergeIds})\n             AND hral.auth_type_code = '${authorityTypeCode}'\n             AND (EXISTS (SELECT 1\n                            FROM hiam_role_auth_data hrad\n                       LEFT JOIN hiam_role_auth_data_line hradl ON hrad.auth_data_id = hradl.auth_data_id\n                           WHERE hrad.role_id = hral.role_id\n                             AND hrad.tenant_id = #{tenantId}\n                             AND hrad.authority_type_code = hral.auth_type_code\n                             AND (hrad.include_all_flag = 1 OR hradl.data_id = #{tableAlias}.${fieldName})\n                             AND NOT EXISTS (SELECT 1\n                                               FROM hiam_user_authority hua\n                                              WHERE hua.tenant_id = hrad.tenant_id\n                                                AND hua.user_id = #{userId}\n                                                AND hua.authority_type_code = hral.auth_type_code))\n                  OR EXISTS (SELECT 1\n                               FROM hiam_user_authority hua1\n                          LEFT JOIN hiam_user_authority_line hual1 ON hua1.authority_id = hual1.authority_id\n                              WHERE hua1.tenant_id = #{tenantId}\n                                AND hua1.user_id = #{userId}\n                                AND hua1.authority_type_code = hral.auth_type_code\n                                AND (hua1.include_all_flag = 1 OR hual1.data_id = #{tableAlias}.${fieldName}))))\n   OR NOT EXISTS (SELECT 1\n                    FROM hiam_role_authority_line hral2\n                   WHERE hral2.role_id IN (#{roleMergeIds})\n                     AND hral2.auth_type_code = '${authorityTypeCode}'))";
    private DocTypeRepository docTypeRepository;
    private DocTypePermissionRepository docTypePermissionRepository;
    private DocTypeAssignService docTypeAssignService;
    private PermissionRuleFeignClient permissionRuleFeignClient;

    @Autowired
    private SrmLovValueRepository srmLovValueRepository;

    @Autowired
    public DocTypeServiceImpl(DocTypeRepository docTypeRepository, DocTypePermissionRepository docTypePermissionRepository, DocTypeAssignService docTypeAssignService, PermissionRuleFeignClient permissionRuleFeignClient) {
        this.docTypeRepository = docTypeRepository;
        this.docTypePermissionRepository = docTypePermissionRepository;
        this.docTypeAssignService = docTypeAssignService;
        this.permissionRuleFeignClient = permissionRuleFeignClient;
    }

    @Override
    public Page<DocType> pageDocType(Long tenantId, String docTypeCode, String docTypeName, PageRequest pageRequest) {
        Page<DocType> docTypeList = this.docTypeRepository.pageDocType(tenantId, docTypeCode, docTypeName, pageRequest);
        if (CollectionUtils.isEmpty(docTypeList)) {
            return docTypeList;
        } else {
            docTypeList.forEach((docType) -> {
                docType.setDocTypeAssigns(this.docTypeAssignService.listAssign(tenantId, docType.getDocTypeId())).setDocTypePermissions(this.docTypePermissionRepository.listPermission(tenantId, docType.getDocTypeId()));
            });
            return docTypeList;
        }
    }

    @Override
    public DocType queryDocType(Long tenantId, Long docTypeId, boolean includeAssign) {
        return this.docTypeRepository.queryDocType(tenantId, docTypeId, includeAssign);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public DocType createDocType(DocType docType) {
        this.docTypeRepository.insertSelective(docType);
        this.docTypeAssignService.createAssign(docType.getDocTypeId(), docType.getDocTypeAssigns());
        this.generateShieldRule(docType.getTenantId(), Collections.singletonList(docType.getDocTypeId()));
        return docType;
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public DocType updateDocType(DocType docType) {
        this.docTypeRepository.updateOptional(docType, DocType.UPDATABLE_FIELD);
        if (ObjectUtils.equals(docType.getLevelCode(), "GLOBAL")) {
            docType.setDocTypeAssigns(Collections.emptyList());
        }

        this.docTypeAssignService.updateAssign(docType.getDocTypeId(), docType.getDocTypeAssigns());
        this.generateShieldRule(docType.getTenantId(), Collections.singletonList(docType.getDocTypeId()));
        return docType;
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void generateShieldRule(Long tenantId, List<Long> docTypeIds) {
        if (!CollectionUtils.isEmpty(docTypeIds)) {
            List<Pair<DataPermissionRangeVO, DataPermissionRuleVO>> dataPermissionList = new ArrayList();
            Map<String, Long> authDimPermission = new HashMap(docTypeIds.size());
            List<DocType> docTypeList = this.docTypeRepository.queryDocTypeWithDimension(tenantId, docTypeIds);
            docTypeList.forEach((docTypex) -> {
                if (!CollectionUtils.isEmpty(docTypex.getDocTypeAuthDims())) {
                    Set<Long> tenantIds = null;
                    if ("GLOBAL".equals(docTypex.getLevelCode())) {
                        tenantIds = Collections.singleton(BaseConstants.DEFAULT_TENANT_ID);
                    } else if (!CollectionUtils.isEmpty(docTypex.getDocTypeAssigns())) {
                        tenantIds = (Set)docTypex.getDocTypeAssigns().stream().map(DocTypeAssign::getAssignValueId).collect(Collectors.toSet());
                    }

                    if (CollectionUtils.isEmpty(tenantIds)) {
                        logger.error("Error generate shield rule, because not assign to any tenant![docTypeId = {}]", docTypex.getDocTypeId());
                    } else {
                        Iterator var5 = docTypex.getDocTypeAuthDims().iterator();

                        while(var5.hasNext()) {
                            DocTypeAuthDim docTypeAuthDim = (DocTypeAuthDim)var5.next();
                            if (!Flag.NO.equals(docTypex.getEnabledFlag()) && !StringUtils.isEmpty(docTypeAuthDim.getSourceMatchTable()) && !StringUtils.isEmpty(docTypeAuthDim.getSourceMatchField())) {
                                String desc = MessageAccessor.getMessage("doc.type.permission.description", new Object[]{docTypex.getDocTypeCode()}).desc();


                                tenantIds.forEach((itemTenant) -> {
                                    String codeRuleKey = this.getCodeRuleKey(docTypex.getDocTypeId(), docTypeAuthDim.getAuthDimId(), itemTenant);
                                    authDimPermission.put(codeRuleKey, docTypeAuthDim.getAuthDimId());
                                    //获取维度类别
                                    SrmLovValue srmLovValue = srmLovValueRepository.selectOne(new SrmLovValue(LOV_CODE,docTypeAuthDim.getAuthTypeCode()));

                                    //限定个人范围情况下
                                    if(srmLovValue.getParentValue().equals("USER")){
                                        dataPermissionList.add(Pair.of((new DataPermissionRangeVO()).setTableName(docTypeAuthDim.getSourceMatchTable()).setEnabledFlag(Flag.YES).setTenantId(itemTenant).setSqlId(docTypex.getSourceDataEntity()).setDescription(desc).setServiceName(docTypex.getSourceServiceName()).setCustomRuleFlag(Flag.NO), (new DataPermissionRuleVO()).setRuleCode(codeRuleKey).setRuleName(docTypex.getDocTypeName()).setDescription(desc).setSqlValue(this.getUserSql(docTypex.getAuthControlType(), docTypeAuthDim.getSourceMatchField(), docTypeAuthDim.getAuthTypeCode(), docTypex.getDocTypeCode(), docTypex.getTenantId() == null? 0L : docTypex.getTenantId())).setTenantId(itemTenant).setEnabledFlag(Flag.YES)));
                                    }else{
                                        dataPermissionList.add(Pair.of((new DataPermissionRangeVO()).setTableName(docTypeAuthDim.getSourceMatchTable()).setEnabledFlag(Flag.YES).setTenantId(itemTenant).setSqlId(docTypex.getSourceDataEntity()).setDescription(desc).setServiceName(docTypex.getSourceServiceName()).setCustomRuleFlag(Flag.NO), (new DataPermissionRuleVO()).setRuleCode(codeRuleKey).setRuleName(docTypex.getDocTypeName()).setDescription(desc).setSqlValue(this.getSql(docTypex.getAuthControlType(), docTypeAuthDim.getSourceMatchField(), docTypeAuthDim.getAuthTypeCode(), docTypex.getDocTypeCode(), docTypex.getTenantId() == null? 0L : docTypex.getTenantId())).setTenantId(itemTenant).setEnabledFlag(Flag.YES)));
                                    }

                                });
                            }
                        }

                    }
                }
            });
            List<Pair<DataPermissionRangeVO, DataPermissionRuleVO>> response = Collections.emptyList();
            if (!CollectionUtils.isEmpty(dataPermissionList)) {
                response = (List)ResponseUtils.getResponse(this.permissionRuleFeignClient.save(tenantId, dataPermissionList), new TypeReference<List<Pair<DataPermissionRangeVO, DataPermissionRuleVO>>>() {
                });
            }

            Set<List<Long>> resultSet = (Set)response.stream().map((dataPermission) -> {
                return Arrays.asList((Long)authDimPermission.get(((DataPermissionRuleVO)dataPermission.getSecond()).getRuleCode()), ((DataPermissionRangeVO)dataPermission.getFirst()).getRangeId(), ((DataPermissionRuleVO)dataPermission.getSecond()).getRuleId());
            }).collect(Collectors.toSet());
            Set<List<Long>> presenceSet = new HashSet();
            List<Long> disablePermissionRuleList = new ArrayList();
            Iterator var10 = docTypeList.iterator();

            while(true) {
                DocType docType;
                do {
                    if (!var10.hasNext()) {
                        if (!CollectionUtils.isEmpty(disablePermissionRuleList)) {
                            this.permissionRuleFeignClient.disablePermission(tenantId, disablePermissionRuleList);
                        }

                        resultSet.forEach((result) -> {
                            if (!presenceSet.contains(result)) {
                                this.docTypePermissionRepository.insertSelective((new DocTypePermission()).setRangeId((Long)result.get(1)).setRuleId((Long)result.get(2)).setAuthDimId((Long)result.get(0)));
                            }

                        });
                        return;
                    }

                    docType = (DocType)var10.next();
                } while(CollectionUtils.isEmpty(docType.getDocTypePermissions()));

                List presence;
                for(Iterator var12 = docType.getDocTypePermissions().iterator(); var12.hasNext(); presenceSet.add(presence)) {
                    DocTypePermission docTypePermission = (DocTypePermission)var12.next();
                    presence = Arrays.asList(docTypePermission.getAuthDimId(), docTypePermission.getRangeId(), docTypePermission.getRuleId());
                    if (!resultSet.contains(presence)) {
                        //todo
                        disablePermissionRuleList.add(Long.valueOf(String.valueOf(presence.get(2))));
                        this.docTypePermissionRepository.deleteByPrimaryKey(docTypePermission);
                    }
                }
            }
        }
    }

    /**
     * 获取限定业务范围下的sql
     *
     * @param authControlType,field,authType
     * @author Summer 2019-12-12 10:12
     * @return
     */
    private String getSql(String authControlType, String field, String authType, String docTypeCode, Long tenantTd) {
        return "ONLY_CONFIG".equals(authControlType) ? ("(EXISTS (SELECT 1 \n" +
                "            FROM hiam_role_authority_line hral\n" +
                " left join hiam_role_authority hira on hira.role_auth_id = hral.role_auth_id\n" +
                " left join hiam_doc_type hidc on hidc.doc_type_id = hira.auth_doc_type_id\n" +
                "           WHERE hral.role_id IN (#{roleMergeIds})\n" +
                "             AND hidc.doc_type_code = '${docTypeCode}'\n" +
                "             AND hidc.tenant_id = ${tenantTd}\n" +
                "             AND hira.auth_scope_code = 'BIZ'\n" +
                "             AND hral.auth_type_code = '${authorityTypeCode}'\n" +
                "             AND (EXISTS (SELECT 1\n" +
                "                            FROM hiam_role_auth_data hrad\n" +
                "                       LEFT JOIN hiam_role_auth_data_line hradl ON hrad.auth_data_id = hradl.auth_data_id\n" +
                "                           WHERE hrad.role_id = hral.role_id\n" +
                "                             AND hrad.tenant_id = #{tenantId}\n" +
                "                             AND hrad.authority_type_code = hral.auth_type_code\n" +
                "                             AND (hrad.include_all_flag = 1 OR hradl.data_id = #{tableAlias}.${fieldName})\n" +
                "                             AND NOT EXISTS (SELECT 1\n" +
                "                                               FROM hiam_user_authority hua\n" +
                "                                              WHERE hua.tenant_id = hrad.tenant_id\n" +
                "                                                AND hua.user_id = #{userId}\n" +
                "                                                AND hua.authority_type_code = hral.auth_type_code))\n" +
                "                  OR EXISTS (SELECT 1\n" +
                "                               FROM hiam_user_authority hua1\n" +
                "                          LEFT JOIN iam_user iam1 ON iam1.id = hua1.user_id\n" +
                "                          LEFT JOIN hiam_user_authority_line hual1 ON hua1.authority_id = hual1.authority_id\n" +
                "                              WHERE hua1.tenant_id = iam1.organization_id\n" +
                "                                AND hua1.user_id = #{userId}\n" +
                "                                AND hua1.authority_type_code = hral.auth_type_code\n" +
                "                                AND (hua1.include_all_flag = 1 OR hual1.data_id = #{tableAlias}.${fieldName}))))\n" +
                "   OR NOT EXISTS (SELECT 1\n" +
                "                    FROM hiam_role_authority_line hral2\n" +
                "                         left join hiam_role_authority hira on hira.role_auth_id = hral2.role_auth_id\n" +
                "                         left join hiam_doc_type hidc on hidc.doc_type_id = hira.auth_doc_type_id\n" +
                "                   WHERE hral2.role_id IN (#{roleMergeIds})\n" +
                "                     AND hidc.doc_type_code = '${docTypeCode}'\n".replace("${docTypeCode}", docTypeCode) +
                "                     AND hira.auth_scope_code = 'BIZ'\n" +
                "                     AND hidc.tenant_id = "+tenantTd+"\n" +
                "                     AND hral2.auth_type_code = '${authorityTypeCode}'))").replace("${fieldName}", field).replace("${authorityTypeCode}", authType).replace("${docTypeCode}", docTypeCode).replace("${tenantTd}", tenantTd.toString())
                : ("(EXISTS (SELECT 1 \n" +
                "            FROM hiam_role_authority_line hral\n" +
                "           WHERE hral.role_id IN (#{roleMergeIds})\n" +
                "             AND hral.auth_type_code = '${authorityTypeCode}'\n" +
                "             AND (EXISTS (SELECT 1\n" +
                "                            FROM hiam_role_auth_data hrad\n" +
                "                       LEFT JOIN hiam_role_auth_data_line hradl ON hrad.auth_data_id = hradl.auth_data_id\n" +
                "                           WHERE hrad.role_id = hral.role_id\n" +
                "                             AND hrad.tenant_id = #{tenantId}\n" +
                "                             AND hrad.authority_type_code = hral.auth_type_code\n" +
                "                             AND (hrad.include_all_flag = 1 OR hradl.data_id = #{tableAlias}.${fieldName})\n" +
                "                             AND NOT EXISTS (SELECT 1\n" +
                "                                               FROM hiam_user_authority hua\n" +
                "                                              WHERE hua.tenant_id = hrad.tenant_id\n" +
                "                                                AND hua.user_id = #{userId}\n" +
                "                                                AND hua.authority_type_code = hral.auth_type_code))\n" +
                "                  OR EXISTS (SELECT 1\n                               FROM hiam_user_authority hua1\n" +
                "                          LEFT JOIN iam_user iam1 ON iam1.id = hua1.user_id\n" +
                "                          LEFT JOIN hiam_user_authority_line hual1 ON hua1.authority_id = hual1.authority_id\n" +
                "                              WHERE hua1.tenant_id = iam1.organization_id\n" +
                "                                AND hua1.user_id = #{userId}\n" +
                "                                AND hua1.authority_type_code = hral.auth_type_code\n" +
                "                                AND (hua1.include_all_flag = 1 OR hual1.data_id = #{tableAlias}.${fieldName})))))").replace("${fieldName}", field).replace("${authorityTypeCode}", authType);
    }

    /**
     * 获取限定个人范围下的sql
     *
     * @param authControlType,field,authType
     * @author Summer 2019-12-12 10:13
     * @return
     */
    private String getUserSql(String authControlType, String field, String authType, String docTypeCode, Long tenantTd) {
        return "ONLY_CONFIG".equals(authControlType) ? "(#{userId} = #{tableAlias}.${fieldName}\n".replace("${fieldName}", field) +
                "   OR NOT EXISTS (SELECT 1\n" +
                "                    FROM hiam_role_authority_line hral\n" +
                "                         left join hiam_role_authority hira on hira.role_auth_id = hral.role_auth_id\n" +
                "                         left join hiam_doc_type hidc on hidc.doc_type_id = hira.auth_doc_type_id\n" +
                "                   WHERE hral.role_id IN (#{roleMergeIds})\n" +
                "                     AND hidc.doc_type_code = '${docTypeCode}'\n".replace("${docTypeCode}", docTypeCode) +
                "                     AND hira.auth_scope_code = 'USER'\n" +
                "                     AND hidc.tenant_id = "+tenantTd+"\n" +
                "                     AND hral.auth_type_code = '${authorityTypeCode}'))".replace("${authorityTypeCode}", authType)




                : "#{userId} = #{tableAlias}.${fieldName}".replace("${fieldName}", field);
    }

    private String getCodeRuleKey(long docTypeId, long authDimId, long tenantId) {
        return String.format("%s%04d%04d%04d", "DT", docTypeId, authDimId, tenantId);
    }
}
