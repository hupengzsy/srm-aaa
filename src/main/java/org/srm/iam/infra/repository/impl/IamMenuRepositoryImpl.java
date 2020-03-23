package org.srm.iam.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.iam.api.dto.IamMenuDTO;
import org.srm.iam.domain.entity.Menu;
import org.srm.iam.domain.repository.IamMenuRepository;
import org.srm.iam.infra.mapper.IamMenuMapper;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author zhiwei.zhou@hand-china.com 2019/7/23 15:47
 */
@Component
public class IamMenuRepositoryImpl extends BaseRepositoryImpl<Menu> implements IamMenuRepository {
    private final IamMenuMapper iamMenuMapper;

    @Autowired
    public IamMenuRepositoryImpl(IamMenuMapper iamMenuMapper) {
        this.iamMenuMapper = iamMenuMapper;
    }


    @Override
    public List<Menu> listMenuByTenant(Long tenantId, String menuName, Integer customFlag) {
        return iamMenuMapper.queryMenuByTenant(tenantId, menuName, customFlag);
    }

    @Override
    public List<Menu> listMenuTenantZero(String menuName, Integer customFlag) {
        return iamMenuMapper.queryMenuTenantZero(menuName, customFlag);
    }

    @Override
    public List<IamMenuDTO> listMenu(String menuName) {
        return iamMenuMapper.queryMenu(menuName);
    }
}
