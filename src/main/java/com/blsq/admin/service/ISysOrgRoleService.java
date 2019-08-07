package com.blsq.admin.service;

import com.blsq.admin.common.util.R;
import com.blsq.admin.model.SysOrgRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-30
 */
public interface ISysOrgRoleService extends IService<SysOrgRole> {
    /**
     * 保存、更新组织角色表
     * @param orgId
     * @param roleIds
     * @return
     */
    R saveUpdateOrgRole(Integer orgId, List<Integer> roleIds);

    /**
     * 删除组织角色表
     * @param orgId
     * @return
     */
    R deleteOrgRole(Integer orgId);


}
