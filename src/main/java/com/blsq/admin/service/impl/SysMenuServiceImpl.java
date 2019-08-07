package com.blsq.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.vo.MenuVO;
import com.blsq.admin.mapper.SysRoleMenuMapper;
import com.blsq.admin.model.SysMenu;
import com.blsq.admin.mapper.SysMenuMapper;
import com.blsq.admin.model.SysRoleMenu;
import com.blsq.admin.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;
    /**
     * 通过角色编号查询URL 权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @Override
    @Cacheable(value = "menu_details", key = "#roleId  + '_menu'")
    public List<MenuVO> findMenuByRoleId(Integer roleId) {
        return baseMapper.listMenusByRoleId(roleId);
    }

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"menu_details","menu_role_details"}, allEntries = true)
    public R removeMenuById(Integer id) {
        // 查询父节点为当前节点的节点
        List<SysMenu> menuList = this.list(new QueryWrapper<SysMenu>()
                .lambda().eq(SysMenu::getParentId, id));
        if (CollUtil.isNotEmpty(menuList)) {
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("菜单含有下级不能删除").build();
        }

        sysRoleMenuMapper
                .delete(new QueryWrapper<SysRoleMenu>()
                        .lambda().eq(SysRoleMenu::getMenuId, id));

        //删除当前菜单及其子菜单
        return new R(this.removeById(id));
    }

    /**
     * 更新菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    @Override
    @CacheEvict(value = {"menu_details","menu_role_details"}, allEntries = true)
    public Boolean updateMenuById(SysMenu sysMenu) {
        return this.updateById(sysMenu);
    }


    /**
     * 根据Id,查询当前菜单的，按钮权限
     * @param menuId  菜单Id
     * @return  菜单按钮权限
     */
    @Override
    public List<String> getBtnPermissionById(Integer menuId) {
        return sysMenuMapper.selectBtnPermisstionById(menuId);
    }

    /**
     * 根据Id,查询当前菜单的，所有权限
     * @param menuId  菜单Id
     * @return  菜单权限信息
     */
    @Override
    public List<SysMenu> getMenuPermission(Integer menuId) {
        return sysMenuMapper.selectMenuPermission(menuId);
    }
}
