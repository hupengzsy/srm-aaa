package org.hzero.iam.domain.service;


import org.hzero.iam.domain.entity.MemberRole;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.entity.UserAuthImport;

/**
 * description
 *
 * @author mingzhi.li@hand-china.com
 */
public interface UserRoleImportService {
    String selectRoleCodeFromViewCode(String viewCode);

    void checkAndInitMemberRole(MemberRole memberRole);

    void assignRole(User user);

    boolean memberRoleNotExists(MemberRole memberRole);
}

