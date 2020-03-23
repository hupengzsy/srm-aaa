package org.srm.iam.app.service;

import org.hzero.iam.domain.entity.User;

/**
 * 子账户同步 service
 *
 * @author: shi.yan@china-hand.com
 *
 **/
public interface UserSynchronizeService {

    User synchronizeUser(User user);
}
