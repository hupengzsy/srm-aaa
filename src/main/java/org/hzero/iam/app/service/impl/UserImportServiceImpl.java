package org.hzero.iam.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.exception.CommonException;

import java.io.IOException;

import org.hzero.boot.imported.app.service.IDoImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.iam.app.service.UserImportCreateService;
import org.hzero.iam.app.service.UserService;
import org.hzero.iam.domain.entity.User;
import org.hzero.iam.domain.service.UserRoleImportService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mingzhi.li@hand-china.com
 */
@ImportService(templateCode = "HIAM.ACCOUNT_CREATE")
public class UserImportServiceImpl implements IDoImportService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleImportService userRoleImportService;
    @Autowired
    private UserImportCreateService userImportCreateService;

    public UserImportServiceImpl() {
    }

    @Override
    public Boolean doImport(String data) {
        User user;
        try {
            user = (User) this.objectMapper.readValue(data, User.class);
        } catch (IOException var4) {
            throw new CommonException("error.data_invalid", new Object[0]);
        }

        user.setAnotherPassword(user.getPassword());
        user.setEnabled(true);
        this.userRoleImportService.assignRole(user);
        // 修改账户导入时不发送消息通知
        userImportCreateService.importCreateUser(user);
        return true;
    }
}
