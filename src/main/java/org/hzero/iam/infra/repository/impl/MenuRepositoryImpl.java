package org.hzero.iam.infra.repository.impl;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.hzero.core.helper.LanguageHelper;
import org.hzero.iam.api.dto.MenuSearchDTO;
import org.hzero.iam.api.dto.PermissionCheckDTO;
import org.hzero.iam.api.dto.PermissionSetSearchDTO;
import org.hzero.iam.domain.entity.Menu;
import org.hzero.iam.domain.entity.MenuTl;
import org.hzero.iam.domain.entity.Permission;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.repository.MenuRepository;
import org.hzero.iam.domain.vo.Lov;
import org.hzero.iam.infra.common.utils.HiamMenuUtils;
import org.hzero.iam.infra.common.utils.UserUtils;
import org.hzero.iam.infra.constant.Constants;
import org.hzero.iam.infra.constant.HiamResourceLevel;
import org.hzero.iam.infra.mapper.MenuMapper;
import org.hzero.iam.infra.mapper.RoleMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;

/**
 * @author allen 2018/6/29
 */
@Repository
public class MenuRepositoryImpl extends BaseRepositoryImpl<Menu> implements MenuRepository, InitializingBean {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMapper roleMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuRepositoryImpl.class);

    /**
     * 平台超级管理员ID
     */
    private AtomicLong siteSuperAdminRoleId = new AtomicLong(-1);
    /**
     * 租户超级管理员ID
     */
    private AtomicLong tenantSuperAdminRoleId = new AtomicLong(-1);

    @Override
    public void afterPropertiesSet() throws Exception {
        // 租户超级管理员
        Role tenantSuperRole = roleMapper.selectOne(new Role().setCode(Constants.TENANT_SUPER_ROLE_CODE));
        if (tenantSuperRole != null) {
            tenantSuperAdminRoleId.set(tenantSuperRole.getId());
        } else {
            LOGGER.warn("tenant super role admin not found, roleCode={}", Constants.TENANT_SUPER_ROLE_CODE);
        }

        // 平台超级管理员
        if (siteSuperAdminRoleId.get() < 0) {
            Role siteSuperRole = roleMapper.selectOne(new Role().setCode(Constants.SITE_SUPER_ROLE_CODE));
            if (siteSuperRole != null) {
                siteSuperAdminRoleId.set(siteSuperRole.getId());
            } else {
                LOGGER.warn("site super role admin not found， roleCode={}", Constants.SITE_SUPER_ROLE_CODE);
            }
        }
    }

    @Override
    public List<Menu> selectMenuTreeInSite(MenuSearchDTO menuParams) {
        menuParams = Optional.ofNullable(menuParams).orElse(new MenuSearchDTO());
        menuParams.setupSiteQueryLevel();
        menuParams.setTenantId(Constants.SITE_TENANT_ID);

        List<Menu> menuList = menuMapper.selectMenusByCondition(menuParams);

        return HiamMenuUtils.formatMenuListToTree(menuList, menuParams.getManageFlag());
    }

    @Override
    public List<Menu> selectMenuTreeInTenant(MenuSearchDTO menuParams) {
        menuParams = Optional.ofNullable(menuParams).orElse(new MenuSearchDTO());
        CustomUserDetails self = UserUtils.getUserDetails();
        menuParams.setTenantId(self.getTenantId());
        menuParams.setupOrganizationQueryLevel();
        menuParams.setRoleId(self.getRoleId());
        List<Menu> menuList = menuMapper.selectMenusByCondition(menuParams);

        return HiamMenuUtils.formatMenuListToTree(menuList, menuParams.getManageFlag());
    }

    @Override
    public List<Menu> selectRoleMenuTree(Long roleId, String lang) {
        CustomUserDetails self = UserUtils.getUserDetails();
        // 未传入角色ID则为用户当前角色ID
        //roleId = Optional.ofNullable(roleId).orElse(self.getRoleId());
        lang = StringUtils.defaultIfBlank(lang, LanguageHelper.language());
        List<Long> mergeIds = self.roleMergeIds();
        // 查询角色菜单
        List<Menu> menuList = menuMapper.selectRoleMenus(mergeIds, lang);

        return HiamMenuUtils.formatMenuListToTree(menuList, Boolean.FALSE);
    }

    @Override
    public Page<Menu> selectMenuDirsInSite(MenuSearchDTO menuParams, PageRequest pageRequest) {
        menuParams = Optional.ofNullable(menuParams).orElse(new MenuSearchDTO());
        menuParams.setTenantId(Constants.SITE_TENANT_ID);
        // 平台级查询菜单
        menuParams.setupSiteQueryLevel();

        MenuSearchDTO finalMenuParams = menuParams;
        return PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> menuMapper.selectMenuDirs(finalMenuParams));
    }

    @Override
    public Page<Menu> selectMenuDirsInTenant(MenuSearchDTO menuParams, PageRequest pageRequest) {
        menuParams = Optional.ofNullable(menuParams).orElse(new MenuSearchDTO());
        CustomUserDetails self = UserUtils.getUserDetails();
        menuParams.setTenantId(self.getTenantId());
        // 组织级查询菜单
        menuParams.setupOrganizationQueryLevel();
        menuParams.setLevel(HiamResourceLevel.ORGANIZATION.value());

        MenuSearchDTO finalMenuParams = menuParams;
        return PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> menuMapper.selectMenuDirs(finalMenuParams));
    }

    @Override
    public List<Menu> selectMenuPermissionSet(Long tenantId, Long menuId, PermissionSetSearchDTO permissionSetParam) {
        permissionSetParam = Optional.ofNullable(permissionSetParam).orElse(new PermissionSetSearchDTO());
        permissionSetParam.setParentMenuId(menuId);
        permissionSetParam.setTenantId(tenantId);

        List<Menu> permissionSets = menuMapper.selectMenuPermissionSet(permissionSetParam);

        return HiamMenuUtils.formatMenuListToTree(permissionSets, true);
    }

    @Override
    public Page<Permission> selectPermissionSetPermissions(PermissionSetSearchDTO permissionSetParam, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> menuMapper.selectPermissionSetPermissions(permissionSetParam));
    }

    @Override
    public Page<Lov> selectPermissionSetLovs(PermissionSetSearchDTO permissionSetParam, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> menuMapper.selectPermissionSetLovs(permissionSetParam));
    }

    @Override
    public Page<Permission> selectAssignablePermissions(PermissionSetSearchDTO permissionSetParam, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> menuMapper.selectAssignablePermissions(permissionSetParam));
    }

    @Override
    public Page<Lov> selectAssignableLovs(PermissionSetSearchDTO permissionSetParam, PageRequest pageRequest) {
        Menu param = new Menu();
        param.setId(permissionSetParam.getPermissionSetId());
        param.setTenantId(permissionSetParam.getTenantId());
        int count = this.selectCount(param);
        if (count == 0) {
            throw new CommonException("hiam.error.permissionSet.notFound");
        }

        return PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                () -> menuMapper.selectAssignableLovs(permissionSetParam));
    }

    @Override
    public List<Menu> selectRolePermissionSet(Long currentRoleId, Long allocateRoleId,
                                              PermissionSetSearchDTO permissionSetParam) {
        permissionSetParam = Optional.ofNullable(permissionSetParam).orElse(new PermissionSetSearchDTO());
        permissionSetParam.setCurrentRoleId(currentRoleId);
        permissionSetParam.setAllocateRoleId(allocateRoleId);

        return menuMapper.selectRolePermissionSet(permissionSetParam);
    }

    @Override
    public List<String> selectAccessiblePermissionSets(Long tenantId, String menuCode) {
        CustomUserDetails details = UserUtils.getUserDetails();
        Long roleId = details.getRoleId();

        return menuMapper.selectPermissionSetCodes(tenantId, roleId, menuCode);
    }

    @Override
    public List<PermissionCheckDTO> checkPermissionSets(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }

        CustomUserDetails self = UserUtils.getUserDetails();

        // 先查询
        List<PermissionCheckDTO> checks = menuMapper.checkPermissionSets(self.roleMergeIds(), codes);

        // 主动校验模式, 未查到, 认为无需参与权限控制
        List<PermissionCheckDTO> notExists = codes.stream()
                .filter(c -> checks.stream().noneMatch(p -> p.getCode().endsWith(c)))
                .map(c -> new PermissionCheckDTO(c, false))
                .collect(Collectors.toList());

        checks.addAll(notExists);

        // admin用户、平台/租户超级管理员直接跳过权限检查
        if ((self.getAdmin() != null && self.getAdmin())
                || self.roleMergeIds().contains(tenantSuperAdminRoleId.get())
                || self.roleMergeIds().contains(siteSuperAdminRoleId.get())) {
            /**
             * FIX 超级管理员默认有权限
             * @since 20190924
             * @authour jianbo.li@hand-china.com
             */
            checks.forEach(t -> {
                t.setApprove(true);
                t.setControllerType(null);
            });
        }

        // 返回结果编码处理
        checks.forEach(item -> {
            String code = codes.stream().filter(c -> item.getCode().endsWith(c)).findFirst().orElse(null);
            if (StringUtils.isNotEmpty(code)) {
                item.setCode(code);
            }
        });

        return checks;
    }

    @Override
    public List<Menu> selectMenuTreeForExport(Long tenantId) {
        CustomUserDetails self = UserUtils.getUserDetails();
        Long roleId = self.getRoleId();
        MenuSearchDTO menuParams = new MenuSearchDTO();
        menuParams.setRoleId(roleId);
        menuParams.setTenantId(tenantId);
        menuParams.setupOrganizationQueryLevel();
        List<Menu> menuList = menuMapper.selectMenusDetail(menuParams);
        if (CollectionUtils.isEmpty(menuList)) {
            return menuList;
        }
        //处理多语言 Menu对象只有name是多语言字段
        String language = LanguageHelper.language();
        menuList.forEach(menu -> {
            List<MenuTl> menuTls = menu.getMenuTls();
            if (!CollectionUtils.isEmpty(menuTls)) {
                //构造_tls字段
                Map<String, Map<String, String>> _tls = new HashMap<>(1);
                Map<String, String> languageFieldMap = new HashMap<>(menuTls.size());
                //遍历多语言字段对象，获取当前上下文对应的语言对应的值
                menuTls.forEach(menuTl -> {
                    if (language.equals(menuTl.getLang())) {
                        menu.setName(menuTl.getName());
                    }
                    languageFieldMap.put(menuTl.getLang(), menuTl.getName());
                });
                _tls.put(Menu.FIELD_NAME, languageFieldMap);
                menu.set_tls(_tls);
                menu.setMenuTls(null);
            }
        });
        return HiamMenuUtils.formatMenuListToTree(menuList, true);
    }

    @Override
    public Menu selectMenuUnique(Long tenantId, String code, String level) {
        Menu param = new Menu();
        param.setTenantId(tenantId);
        param.setCode(code);
        param.setLevel(level);
        return menuMapper.selectOne(param);
    }


}
