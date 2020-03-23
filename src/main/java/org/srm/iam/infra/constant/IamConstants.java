package org.srm.iam.infra.constant;

/**
 * 常量
 *
 * @author peng.yang03@hand-china.com 2019/07/31 15:09
 */
public interface IamConstants {

    /**
     * SRM数据源代码
     */
    String SRM_SOURCE_CODE = "SRM";

    String DEFAULT_ASSIGN_LEVEL = "organization";

    String DEFAULT_LANGUAGE = "zh_CN";

    String SUCCESS_STATUS = "SUCCESS";

    String FAILURE_STATUS = "FAILURE";

    /**
     * 业务异常
     */
    public static class ErrorCode {
        private ErrorCode() {
        }

        /**
         * 企业注册: 无法创建租户
         */
        public static final String COMPANY_REGISTER_TENANT_CREATE_ERROR = "company.register.create_tenant_error";

        /**
         * 企业注册: 无法创建或更新公司
         */
        public static final String COMPANY_REGISTER_COMPANY_MODIFY_ERROR = "company.register.modify_company_error";

        /**
         * 企业注册: 无法分配租户默认角色
         */
        public static final String COMPANY_REGISTER_ROLE_ASSIGN_ERROR = "company.register.role_assign_error";

        /**
         * 企业注册: 无法创建集团
         */
        public static final String COMPANY_REGISTER_GROUP_CREATE_ERROR = "company.register.create_group_error";

        /**
         * 企业注册: 用户分配租户失败
         */
        public static final String COMPANY_REGISTER_TENANT_ASSIGN_ERROR = "company.register.tenant_assign_error";

        /**
         * 企业注册：权限分配失败
         */
        public static final String CREATE_ROLE_AUTHORITY_ERROR = "create.role.authority.error";

        /**
         * saga数据初始化异常
         */
        public static final String SAGA_INIT_ERROR = "saga.init.error";

        /**
         * tenant暂未同步
         */
        public static final String PLEASE_IMPORT_TENANT_FIRST = "error.please.import.tenant.first";

        /**
         * 分配的角色不存在
         */
        public static final String USER_ASSIGNED_ROLE_NOT_EXIST = "error.user.assigned.role.not.exist";

        /**
         * 租户未初始化租户管理员
         */
        public static final String ADMINISTRATOR_HAVE_NOT_INIT = "error.administrator.have.not.init";

        /**
         * 用户没有租户管理员角色
         */
        public static final String USER_DOESNT_HAVE_ADMIN_ROLE = "error.user_does_not_have_admin_role";

        public static final String USER_AUTHORITY_NOT_SELECT = "error.user_authority_import_valid";

    }

    /**
     * 用户权限类型编码
     */
    interface OrgTypeCode {
        /**
         * 公司
         */
        String COMPANY = "COMPANY";
        /**
         * 业务实体
         */
        String OU = "OU";
        /**
         * 库存组织
         */
        String INVORG = "INVORG";
        /**
         * 客户
         */
        String CUSTOMER = "CUSTOMER";
        /**
         * 供应商
         */
        String SUPPLIER = "SUPPLIER";
        /**
         * 采购组织
         */
        String PURORG = "PURCHASE_ORGANIZATION";
        /**
         * 采购员
         */
        String PURAGENT = "PURCHASE_AGENT";
        /**
         * 采购品类
         */
        String PURCAT = "PURCHASE_CATEGORY";
        /**
         * 采购物料
         */
        String PURITEM = "PURCHASE_ITEM";
        /**
         * 销售产品
         */
        String SALITEM = "SUPPLIER_ITEM";
        /**
         * 组织架构-公司
         */
        String GROUP = "GROUP";
        /**
         * 部门
         */
        String UNIT = "UNIT";
        /**
         * 岗位
         */
        String POSITION = "POSITION";
        /**
         * 员工
         */
        String EMPLOYEE = "EMPLOYEE";

        /**
         * SRM 值集中的库存组织
         */
        String INV_ORGANIZATION = "INV_ORGANIZATION";
    }


}
