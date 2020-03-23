package org.hzero.iam.domain.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants.Flag;
import org.hzero.iam.domain.entity.*;
import org.hzero.iam.domain.repository.*;
import org.hzero.iam.domain.service.UserRoleImportService;
import org.hzero.iam.domain.vo.RoleVO;
import org.hzero.iam.infra.common.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author mingzhi.li@hand-china.com
 */
public class UserRoleImportServiceImpl implements UserRoleImportService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private HpfmHrUnitRepository hrUnitRepository;
    @Autowired
    private MemberRoleRepository memberRoleRepository;

    public UserRoleImportServiceImpl() {
    }

    @Override
    public String selectRoleCodeFromViewCode(String viewCode) {
        RoleVO params = new RoleVO();
        params.setCode(viewCode);
        List<RoleVO> roleList = this.roleRepository.selectSelfAllManageableRoles(params);
        if (CollectionUtils.isEmpty(roleList)) {
            throw new CommonException("hiam.warn.user.import.roleCodeOrNameNotExist", new Object[0]);
        } else {
            roleList.sort((o1, o2) -> {
                return o1.getId() - o2.getId() > 0L ? Flag.YES : -Flag.YES;
            });
            return ((RoleVO)roleList.get(0)).getCode();
        }
    }

    @Override
    public void checkAndInitMemberRole(MemberRole memberRole) {
        memberRole.setMemberType("user");
        User queryUser = new User();
        queryUser.setLoginName(memberRole.getLoginName());
        queryUser.setRealName(memberRole.getRealName());
        User user = (User)this.userRepository.selectOne(queryUser);
        if (user == null) {
            throw new CommonException("user.import.user_login_name_not_exsit", new Object[0]);
        } else {
            memberRole.setMemberId(user.getId());
            Role queryRole = new Role();
            queryRole.setCode(memberRole.getRoleCode());
            queryRole.setName(memberRole.getRoleName());
            Role role = (Role)this.roleRepository.selectOne(queryRole);
            if (role == null) {
                throw new CommonException("hiam.warn.user.import.roleCodeOrNameNotExist", new Object[0]);
            } else {
                memberRole.setRoleId(role.getId());
                String level = memberRole.getAssignLevel();
                if (Objects.equals("organization", level)) {
                    if (StringUtils.isNotEmpty(memberRole.getAssignNum())) {
                        Tenant queryTenant = new Tenant();
                        queryTenant.setTenantNum(memberRole.getAssignNum());
                        Tenant tenant = (Tenant)this.tenantRepository.selectOne(queryTenant);
                        if (tenant == null) {
                            throw new CommonException("user.import.tenant_num_not_exsit", new Object[0]);
                        }

                        memberRole.setAssignLevelValue(tenant.getTenantId());
                    } else {
                        memberRole.setAssignLevelValue(DetailsHelper.getUserDetails().getTenantId());
                    }
                }

                if (Objects.equals("org", level)) {
                    HrUnit hrUnit = new HrUnit();
                    hrUnit.setTenantId(user.getOrganizationId());
                    hrUnit.setUnitCode(memberRole.getAssignNum());
                    hrUnit = (HrUnit)this.hrUnitRepository.selectOne(hrUnit);
                    if (hrUnit == null) {
                        throw new CommonException("user.import.unit_code_not_exsit", new Object[0]);
                    }

                    memberRole.setAssignLevelValue(hrUnit.getUnitId());
                }

            }
        }
    }

    @Override
    public void assignRole(User user) {
        String roleCode = this.selectRoleCodeFromViewCode(user.getRoleCode());
        Role role = new Role();
        if (StringUtils.isNotEmpty(roleCode)) {
            user.setRoleCode(roleCode);
            role.setCode(roleCode);
            role.setTenantId(DetailsHelper.getUserDetails().getTenantId());
            role = (Role)this.roleRepository.selectOne(role);
            AssertUtils.notNull(role, "hiam.warn.user.import.roleCodeOrNameNotExist", new Object[0]);
        } else {
            role.setId(DetailsHelper.getUserDetails().getRoleId());
            role = (Role)this.roleRepository.selectByPrimaryKey(DetailsHelper.getUserDetails().getRoleId());
        }

        MemberRole memberRole = new MemberRole();
        memberRole.setRoleId(role.getId());
        memberRole.setRoleCode(role.getCode());
        memberRole.setRoleName(role.getName());
        memberRole.setAssignLevel("organization");
        memberRole.setAssignLevelValue(role.getTenantId());
        user.setMemberRoleList(Collections.singletonList(memberRole));
    }

    @Override
    public boolean memberRoleNotExists(MemberRole memberRole) {
        MemberRole params = new MemberRole();
        params.setRoleId(memberRole.getRoleId());
        params.setMemberId(memberRole.getMemberId());
        params.setMemberType(memberRole.getMemberType());
        params.setSourceId(memberRole.getSourceId());
        params.setSourceType(memberRole.getSourceType());
        return this.memberRoleRepository.selectCount(params) == 0;
    }
}
