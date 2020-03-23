//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hzero.iam.domain.service.role;

import io.choerodon.core.exception.CommonException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.iam.domain.entity.MemberRole;
import org.hzero.iam.domain.entity.Role;
import org.hzero.iam.domain.entity.RolePermission;
import org.hzero.iam.domain.entity.Tenant;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.repository.MemberRoleRepository;
import org.hzero.iam.domain.repository.RolePermissionRepository;
import org.hzero.iam.domain.repository.RoleRepository;
import org.hzero.iam.domain.repository.TenantRepository;
import org.hzero.iam.domain.repository.UserRepository;
import org.hzero.iam.domain.vo.RoleVO;
import org.hzero.iam.infra.constant.HiamMemberType;
import org.hzero.iam.infra.constant.HiamResourceLevel;
import org.hzero.iam.infra.constant.RolePermissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RoleCreateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleCreateService.class);
    @Autowired
    protected MemberRoleRepository memberRoleRepository;
    @Autowired
    protected TenantRepository tenantRepository;
    @Autowired
    protected RolePermissionRepository rolePermissionRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    public RoleCreateService() {
    }

    public Role createRole(Role role, User adminUser, boolean inherited, boolean duplicate) {
        LOGGER.info(">> create role before: role={}, adminUser={}, inherited={}, duplicate={}", new Object[]{role, adminUser, inherited, duplicate});
        this.checkAdminUser(role, adminUser);
        this.checkCreateType(role, inherited, duplicate);
        this.checkAdminRole(role, adminUser);
        this.checkRoleTenant(role);
        this.checkRoleLevel(role);
        this.checkRoleName(role);
        if (inherited) {
            this.checkInheritRoleRole(role);
        } else if (duplicate) {
            this.checkDuplicateRole(role);
        }

        this.setupRoleCode(role, adminUser);
        this.checkRoleExists(role, adminUser);
        this.handleRoleLevelPath(role);
        this.handleBeforeCreate(role, adminUser);
        this.persistRole(role);
        this.handleRolePermission(role);
        this.postHandle(role);
        LOGGER.info(">> create role success: {}", role);
        return role;
    }

    protected void checkAdminUser(Role role, User adminUser) {
        if (adminUser != null && adminUser.getId() != null) {
            User tmp = this.userRepository.selectUserTenant(adminUser.getId());
            if (tmp == null) {
                throw new CommonException("hiam.warn.role.userWithTenantNotFound", new Object[0]);
            } else {
                adminUser.setTenantNum(tmp.getTenantNum());
                adminUser.setOrganizationId(tmp.getOrganizationId());
                role.setCreatedByTenantId(adminUser.getOrganizationId());
                role.setCreatedByTenantNum(adminUser.getTenantNum());
            }
        } else {
            throw new CommonException("hiam.warn.role.adminUserIsNull", new Object[0]);
        }
    }

    protected void checkCreateType(Role role, boolean inherited, boolean duplicate) {
        if (inherited) {
            if (role.getInheritRoleId() == null) {
                throw new CommonException("hiam.warn.role.inheritedRoleIdMustNotBeNull", new Object[0]);
            }

            role.setCopyFromRoleId((Long)null);
        } else if (duplicate) {
            if (role.getCopyFromRoleId() == null) {
                throw new CommonException("hiam.warn.role.duplicateRoleIdMustNotBeNull", new Object[0]);
            }

            role.setInheritRoleId((Long)null);
        }

    }

    protected Role checkAdminRole(Role role, User adminUser) {
        Role parentRole = (Role)this.roleRepository.selectByPrimaryKey(role.getParentRoleId());
        if (parentRole == null) {
            throw new CommonException("hiam.warn.role.parentRoleNotFound", new Object[0]);
        } else {
            RoleVO params = new RoleVO();
            params.setId(parentRole.getId());
            List<RoleVO> selfAdminRoles = this.roleRepository.selectSelfAdminRoles(params);
            if (CollectionUtils.isEmpty(selfAdminRoles)) {
                throw new CommonException("hiam.warn.role.parentRoleIsNotAdminRole", new Object[0]);
            } else {
                MemberRole memberRole = this.memberRoleRepository.getByRoleIdAndMemberId(role.getParentRoleId(), adminUser.getId(), HiamMemberType.USER);
                if (memberRole == null) {
                    throw new CommonException("hiam.warn.role.memberRoleNotFound", new Object[0]);
                } else {
                    role.setupParentAssignLevel(memberRole);
                    role.setParentRole(parentRole);
                    return parentRole;
                }
            }
        }
    }

    protected void checkRoleTenant(Role role) {
        Role adminRole = role.getParentRole();
        if (Objects.equals(adminRole.getTenantId(), BaseConstants.DEFAULT_TENANT_ID) && StringUtils.equals(adminRole.getLevel(), HiamResourceLevel.ORGANIZATION.value())) {
            if (role.getInheritRole() != null && !Objects.equals(role.getTenantId(), role.getInheritRole().getTenantId())) {
                throw new CommonException("hiam.warn.role.roleTenantIdEqualsInheritRoleTenantId", new Object[0]);
            }
        } else if (!Objects.equals(role.getTenantId(), adminRole.getTenantId())) {
            throw new CommonException("hiam.warn.role.roleTenantIdEqualsParentRoleTenantId", new Object[0]);
        }

        if (role.getTenantId() == null) {
            role.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        }

        Tenant tenant = (Tenant)this.tenantRepository.selectByPrimaryKey(role.getTenantId());
        if (tenant == null) {
            throw new CommonException("hiam.warn.role.tenantNotFound", new Object[0]);
        } else {
            role.setTenantNum(tenant.getTenantNum());
        }
    }

    protected void checkRoleLevel(Role role) {
        Role adminRole = role.getParentRole();
        if (StringUtils.isNotBlank(role.getLevel()) && !StringUtils.equalsIgnoreCase(role.getLevel(), adminRole.getLevel())) {
            throw new CommonException("hiam.warn.role.roleLevelEqualsAdminRoleLevel", new Object[0]);
        } else {
            role.setLevel(adminRole.getLevel());
        }
    }

    protected void checkRoleName(Role role) {
    }

    protected void checkInheritRoleRole(Role role) {
        Role inheritRole = (Role)this.roleRepository.selectByPrimaryKey(role.getInheritRoleId());
        if (inheritRole == null) {
            throw new CommonException("hiam.warn.role.inheritedRoleNotFound", new Object[0]);
        } else {
            role.setCopyFromRoleId((Long)null);
            role.setInheritRole(inheritRole);
        }
    }

    protected void checkDuplicateRole(Role role) {
        Role duplicateRole = (Role)this.roleRepository.selectByPrimaryKey(role.getCopyFromRoleId());
        if (duplicateRole == null) {
            throw new CommonException("hiam.warn.role.duplicateRoleNotFound", new Object[0]);
        } else {
            role.setInheritRoleId((Long)null);
            role.setCopyRole(duplicateRole);
        }
    }

    /** @deprecated */
    @Deprecated
    public String generateRoleCode(Role role, User adminUser) {
        StringBuilder codeBuilder = new StringBuilder(HiamResourceLevel.levelOf(role.getLevel()).code());
        codeBuilder.append(String.format("%s.%s.%s.%s.%s", role.getTenantNum(), role.getParentRoleId(), role.getParentRoleAssignLevel(), role.getParentRoleAssignLevelValue(), adminUser.getTenantNum())).append("/");
        codeBuilder.append(role.getCode());
        return codeBuilder.toString();
    }

    protected void setupRoleCode(Role role, User adminUser) {
    }

    protected void checkRoleExists(Role role, User adminUser) {
        Role params = new Role();
        params.setTenantId(role.getTenantId());
        params.setCode(role.getCode());
        params.setParentRoleId(role.getParentRoleId());
        params.setParentRoleAssignLevel(role.getParentRoleAssignLevel());
        params.setParentRoleAssignLevelValue(role.getParentRoleAssignLevelValue());
        params.setCreatedByTenantId(adminUser.getOrganizationId());
        if (this.roleRepository.selectCount(params) > 0) {
            throw new CommonException("hiam.warn.role.codeExist", new Object[0]);
        }
    }

    protected void handleBeforeCreate(Role role, User adminUser) {
        role.setBuiltIn(false);
        role.setEnabled(true);
        role.setAssignable(false);
        role.setEnableForbidden(true);
        role.setModified(true);
        role.setCreatedBy(adminUser.getId());
        role.setCreatedByTenantId(adminUser.getOrganizationId());
    }

    protected void persistRole(Role role) {
        this.roleRepository.insertSelective(role);
    }

    protected void handleRoleLevelPath(Role role) {
        role.buildCreatedRoleLevelPath(role.getParentRole());
        role.buildInheritRoleLevelPath(role.getParentRole(), role.getInheritRole());
    }

    protected void handleRolePermission(Role role) {
        if (CollectionUtils.isNotEmpty(role.getPermissionSets())) {
            role.getPermissionSets().forEach((rp) -> {
                rp.setRoleId(role.getId());
            });
        } else {
            role.setPermissionSets(new ArrayList(1024));
        }

        if (role.getInheritRole() != null) {
            this.initInheritRolePermissionSets(role);
        } else if (role.getCopyRole() != null) {
            this.initDuplicateRolePermissionSets(role);
        }

        role.getPermissionSets().parallelStream().forEach((rps) -> {
            this.rolePermissionRepository.insertSelective(rps);
        });
    }

    protected void initInheritRolePermissionSets(Role role) {
        RolePermission params = new RolePermission();
        params.setRoleId(role.getInheritRoleId()).setLevel(role.getLevel());
        List<RolePermission> inheritRolePermissionSets = this.rolePermissionRepository.selectRolePermissionSets(params);
        List<RolePermission> rolePermissionSets = new ArrayList(inheritRolePermissionSets.size());
        Long roleId = role.getId();
        /**
         * 覆盖源码中的 parallelStream
         * 多线程操作 非线程安全的 arraylist ，会造成数据丢失问题，此处使用 stream
         */
        inheritRolePermissionSets.stream().forEach((rps) -> {
            String inheritFlag;
            if (StringUtils.equalsAny("Y", new CharSequence[]{rps.getCreateFlag(), rps.getInheritFlag()})) {
                inheritFlag = "Y";
            } else {
                inheritFlag = "X";
            }

            rolePermissionSets.add(new RolePermission(roleId, rps.getPermissionSetId(), inheritFlag, "N", RolePermissionType.PS.name()));
        });
        LOGGER.info("inherit {} permissions from role.", rolePermissionSets.size());
        role.getPermissionSets().addAll(rolePermissionSets);
    }

    protected void initDuplicateRolePermissionSets(Role role) {
        RolePermission params = new RolePermission();
        params.setRoleId(role.getCopyFromRoleId()).setLevel(role.getLevel());
        List<RolePermission> duplicateRolePermissionSets = this.rolePermissionRepository.selectRolePermissionSets(params);
        List<RolePermission> rolePermissionSets = new ArrayList(duplicateRolePermissionSets.size());
        Long roleId = role.getId();
        duplicateRolePermissionSets.stream().forEach((item) -> {
            if (StringUtils.equalsAny("Y", new CharSequence[]{item.getCreateFlag(), item.getInheritFlag()})) {
                rolePermissionSets.add(new RolePermission(roleId, item.getPermissionSetId(), "N", "Y", RolePermissionType.PS.name()));
            }

        });
        LOGGER.info("copy {} permissions from role.", rolePermissionSets.size());
        role.getPermissionSets().addAll(rolePermissionSets);
    }

    protected void postHandle(Role role) {
        role.setPermissionSets((List)null);
    }
}
