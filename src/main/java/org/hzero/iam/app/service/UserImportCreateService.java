package org.hzero.iam.app.service;

import org.hzero.iam.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 子账户导入 service
 * </p>
 *
 * @author: shi.yan@hand-china.com 2020/2/5
 */
public interface UserImportCreateService{

    void importCreateUser(User paramUser);
}
