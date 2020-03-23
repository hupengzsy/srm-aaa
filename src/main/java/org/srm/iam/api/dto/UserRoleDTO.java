package org.srm.iam.api.dto;

import java.util.List;

import org.srm.iam.domain.entity.Role;

/**
 * <p>
 * 用户所有租户及角色信息
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/19 15:18
 */
public class UserRoleDTO {
    private Long organizationId;

    private String cropName;

    private List<Role> roles;

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserRoleDTO{" +
                "organizationId=" + organizationId +
                ", cropName='" + cropName + '\'' +
                ", roles=" + roles +
                '}';
    }
}
