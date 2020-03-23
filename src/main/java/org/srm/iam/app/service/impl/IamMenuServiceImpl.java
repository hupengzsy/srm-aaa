package org.srm.iam.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.iam.api.dto.IamMenuDTO;
import org.srm.iam.app.service.IamMenuService;
import org.srm.iam.domain.entity.Menu;
import org.srm.iam.domain.repository.IamMenuRepository;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 15:55
 */
@Service
public class IamMenuServiceImpl implements IamMenuService {
    private final IamMenuRepository iamMenuRepository;

    @Autowired
    public IamMenuServiceImpl(IamMenuRepository iamMenuRepository) {
        this.iamMenuRepository = iamMenuRepository;
    }


    @Override
    public Page<Menu> listMenuByTenant(boolean siteMenuFlag, Long tenantId, String menuName, Integer customFlag,
                    PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,
                        () -> siteMenuFlag ? iamMenuRepository.listMenuTenantZero(menuName, customFlag)
                                        : iamMenuRepository.listMenuByTenant(tenantId, menuName, customFlag));
    }

    @Override
    public List<IamMenuDTO> listMenu(String menuName) {
        return iamMenuRepository.listMenu(menuName);
    }
}
