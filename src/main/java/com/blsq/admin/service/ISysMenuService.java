package com.blsq.admin.service;

import com.blsq.admin.common.util.R;
import com.blsq.admin.common.vo.MenuRoleVO;
import com.blsq.admin.common.vo.MenuVO;
import com.blsq.admin.model.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 通过角色编号查询URL 权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuVO> findMenuByRoleId(Integer roleId);

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return 成功、失败
     */
    R removeMenuById(Integer id);

    /**
     * 更新菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    Boolean updateMenuById(SysMenu sysMenu);


    /**
     * 根据Id,查询当前菜单的，按钮权限
     * @param menuId  菜单Id
     * @return  菜单权限信息
     */
    List<String> getBtnPermissionById(Integer menuId);

    /**
     * 根据Id,查询当前菜单的，所有权限
     * @param menuId  菜单Id
     * @return  菜单按钮权限
     */
    List<SysMenu> getMenuPermission(Integer menuId);
}
