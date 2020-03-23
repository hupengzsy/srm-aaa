package org.srm.iam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/22 9:55
 */
@Configuration
public class IamSwaggerApiConfig {
    /**
     * 用户角色
     */
    public static final String USER_ROLE = "User Role";

    /**
     * 租户角色
     */
    public static final String TENANT_ROLE = "Tenant Role";


    /**
     * 菜单相关
     */
    public static final String MENU = "Menu(Site Level)";
    public static final String TENANT_MENU = "Tenant Menu(Org Level)";

    /**
     * 公司审批
     */
    public static final String APPROVE_COMPANY = "Approve Company(Site Menu)";

    /**
     * 用户权限包裹
     */
    public static final String USER_AUTHORITY_WRAPPER = "User Authority Wrapper(Org Level)";

    /**
     * 角色权限包裹
     */
    public static final String ROLE_AUTH_DATA_WRAPPER = "Role Auth Data Wrapper(Org Level)";
    /**
     * 供应商导入
     */
    public static final String COMPANY_IMPORT = "Company Import";

    @Autowired
    public IamSwaggerApiConfig(Docket docket) {
        docket.tags(
                new Tag(USER_ROLE, "用户角色"),
                new Tag(TENANT_ROLE, "租户角色"),

                new Tag(MENU, "菜单（平台级）"),
                new Tag(TENANT_MENU, "租户菜单（租户级）"),
                new Tag(APPROVE_COMPANY, "公司审批(平台级)"),

                new Tag(USER_AUTHORITY_WRAPPER,"用户权限封装(租户级)"),
                new Tag(ROLE_AUTH_DATA_WRAPPER,"角色权限封装(租户级)"),
                new Tag(COMPANY_IMPORT, "公司导入")
        );
    }
}
