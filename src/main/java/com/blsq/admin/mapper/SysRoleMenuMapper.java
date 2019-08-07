package com.blsq.admin.mapper;

import com.blsq.admin.common.vo.MenuRoleVO;
import com.blsq.admin.model.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     *
     * @return
     */
    List<MenuRoleVO> selectAllMenu();
}
