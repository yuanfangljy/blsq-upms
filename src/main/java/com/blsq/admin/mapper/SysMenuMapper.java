package com.blsq.admin.mapper;

import com.blsq.admin.common.vo.MenuVO;
import com.blsq.admin.model.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    /**
     * 根据角色编号查询用户信息
     * @param roleCode
     * @return
     */
    List<MenuVO> selectMenuByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编号查询菜单
     *
     * @param roleId 角色ID
     * @return
     */
    List<MenuVO> listMenusByRoleId(Integer roleId);

    /**
     * 根据Id,查询当前菜单的，按钮权限
     * @param menuId  菜单Id
     * @return  菜单按钮权限
     */
    List<String> selectBtnPermisstionById(Integer menuId);


    /**
     * 根据Id,查询当前菜单的，所有权限
     * @param menuId  菜单Id
     * @return  菜单权限信息
     */
    List<SysMenu> selectMenuPermission(Integer menuId);
}
