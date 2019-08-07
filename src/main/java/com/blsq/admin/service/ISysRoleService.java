package com.blsq.admin.service;

import com.blsq.admin.common.util.R;
import com.blsq.admin.model.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    List<SysRole> findRolesByUserId(Integer userId);

    /**
     * 通过角色ID，删除角色
     *
     * @param id
     * @return
     */
    Boolean removeRoleById(Integer id);

    /**
     * 获取用户角色信息
     * @return
     */
    Set<Integer> getRoleIds();

    /**
     * 判断角色是否存在
     * @return
     */
    R isRole(SysRole sysRole);
}
