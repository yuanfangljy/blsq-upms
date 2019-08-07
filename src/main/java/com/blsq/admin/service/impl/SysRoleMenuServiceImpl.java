package com.blsq.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.common.vo.MenuRoleVO;
import com.blsq.admin.model.SysRoleMenu;
import com.blsq.admin.mapper.SysRoleMenuMapper;
import com.blsq.admin.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@Service
@AllArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    private final CacheManager cacheManager;

    /**
     * @param role
     * @param roleId  角色
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "menu_details", key = "#roleId + '_menu'")
    public Boolean saveRoleMenus(String role, Integer roleId, String menuIds) {
        this.remove(new QueryWrapper<SysRoleMenu>().lambda()
                .eq(SysRoleMenu::getRoleId, roleId));

        if (StrUtil.isBlank(menuIds)) {
            return Boolean.TRUE;
        }
        List<SysRoleMenu> roleMenuList = Arrays
                .stream(menuIds.split(","))
                .map(menuId -> {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(Integer.valueOf(menuId));
                    return roleMenu;
                }).collect(Collectors.toList());

        //清空userinfo
        cacheManager.getCache("user_details").clear();
        return this.saveBatch(roleMenuList);
    }

    /**
     * 查询所有的菜单以及相关角色信息
     *
     * @return
     */
    @Override

    public List<MenuRoleVO> getAllMenu() {
        return baseMapper.selectAllMenu();
    }
}
