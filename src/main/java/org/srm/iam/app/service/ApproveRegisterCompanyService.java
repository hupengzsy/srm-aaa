package org.srm.iam.app.service;

import org.srm.iam.domain.vo.SslmCompanyVO;

import java.util.Map;

/**
 * 企业注册审批service
 *
 * @author peng.yang@hand-china.com 2019/08/01 10:51
 */
public interface ApproveRegisterCompanyService {

    /**
     * 企业注册审批 为sslm提供feign调用
     *
     * @param sagaKey sagaKey
     * @param companyVO 公司信息
     * @return 用户信息
     */
    Map approveRegisterCompany(String sagaKey, SslmCompanyVO companyVO);

    /**
     * 将企业添加到提交人维度 为角色分配权限
     *
     * @param sagaKey   sagaKey
     * @param companyVO 公司信息
     */
    void createTreeUserAuthorityAndAssignRole(String sagaKey, SslmCompanyVO companyVO);

}
