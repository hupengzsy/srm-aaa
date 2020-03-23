//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.srm.iam.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.hzero.iam.domain.entity.RoleAuthDataLine;
import org.srm.iam.domain.entity.SrmRoleAuthDataLine;

public interface SrmRoleAuthDataLineMapper extends BaseMapper<SrmRoleAuthDataLine> {

    List<RoleAuthDataLine> listRoleAuthDataLine(@Param("authDataId") Long authDataId, @Param("tenantId") Long tenantId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<RoleAuthDataLine> listRoleAuthDataLinePurOrg(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<RoleAuthDataLine> listRoleAuthDataLinePurAgent(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<RoleAuthDataLine> listRoleAuthDataLineLov(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<RoleAuthDataLine> listRoleAuthDataLineLovView(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<RoleAuthDataLine> listRoleAuthDataLineDatasource(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId, @Param("dataCode") String dataCode, @Param("dataName") String dataName);

    List<RoleAuthDataLine> listRoleAuthGroupData(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId, @Param("groupCode") String groupCode, @Param("groupName") String groupName);
}
