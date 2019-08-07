package com.blsq.admin.service;

import com.blsq.admin.common.vo.MenuRoleVO;
import com.blsq.admin.model.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 更新角色菜单
     *
     * @param roleCode 角色编号
     * @param roleId  角色Id
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return
     */
    Boolean saveRoleMenus(String roleCode, Integer roleId, String menuIds);

    /**
     * 查询所有的菜单以及相关角色信息
     * @return
     */
    List<MenuRoleVO> getAllMenu();
}
