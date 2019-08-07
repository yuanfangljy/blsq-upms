package com.blsq.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.dto.MenuTree;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.common.vo.MenuVO;
import com.blsq.admin.common.vo.TreeUtil;
import com.blsq.admin.model.SysMenu;
import com.blsq.admin.model.SysRole;
import com.blsq.admin.service.ISysMenuService;
import com.blsq.admin.service.ISysRoleService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@RestController
@RequestMapping("/sysMenu")
@AllArgsConstructor
@Api(value = "menu", description = "菜单管理模块")
public class SysMenuController {

    private final ISysMenuService sysMenuService;
    private final ISysRoleService sysRoleService;

    /**
     * 返回当前用户的树形菜单集合
     *
     * @return 当前用户的树形菜单
     */
    @PostMapping("/getUserMenu")
    public R getUserMenu() {
        // 获取符合条件的菜单
        Set<MenuVO> all = new HashSet<>();

        SecurityUtils.getRoles()
                .forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
        List<MenuTree> menuTreeList = all.stream()
                .filter(menuVo -> CommonConstants.MENU.equals(menuVo.getType()))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(MenuTree::getSort))
                .collect(Collectors.toList());
        return new R<>(TreeUtil.build(menuTreeList, -1));
    }
    
    /**
     * 返回当前用户的树形菜单集合
     *
     * @return 当前用户的树形菜单
     */
    @GetMapping("/test")
    public R test() {
        // 获取符合条件的菜单
        return new R<>();
    }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @PostMapping(value = "/tree")
    public R getTree() {
        return new R<>(TreeUtil.buildTree(sysMenuService.list(new QueryWrapper<SysMenu>().lambda().ne(SysMenu::getType,1)), -1));
    }


    /**
     * 返回树形菜单及权限集合
     *
     * @return 树形菜单及权限
     */
    @PostMapping(value = "/treePerssion")
    public R getTreePerssion() {
        return new R<>(TreeUtil.buildTree(sysMenuService.list(new QueryWrapper<>()), -1));
    }

    /**
     * 返回角色的菜单集合
     *
     * @param sysRole 角色ID
     * @return 属性集合
     */
    @PostMapping("/roleTree")
    public R getRoleTree(@RequestBody SysRole sysRole) {
        return R.builder().data(sysMenuService.findMenuByRoleId(sysRole.getRoleId())
                .stream()
                .map(MenuVO::getMenuId)
                .collect(Collectors.toList())).build();
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param sysMenu 菜单ID
     * @return 菜单详细信息
     */
    @PostMapping("/getById")
    public R getById(@RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.getById(sysMenu.getMenuId()));
    }

    /**
     * 根据Id,查询当前菜单的，按钮权限
     * @param sysMenu
     * @return
     */
    @PostMapping("/getBtnPermissionById")
    public R getBtnPermissionById(@RequestBody SysMenu sysMenu){
        return new R<>(sysMenuService.getBtnPermissionById(sysMenu.getMenuId()));
    }

    /**
     * 根据Id,查询当前菜单的，所有权限
     * @param sysMenu
     * @return
     */
    @PostMapping("/getMenuPermission")
    public R getMenuPermission(@RequestBody SysMenu sysMenu){
        if(sysMenu.getMenuId()!=null){
            SysMenu byId = sysMenuService.getById(sysMenu.getMenuId());
            if(byId==null){
                return R.builder().code(CommonConstants.FAIL).message("菜单ID，不存在").build();
            }
        }
        return new R<>(sysMenuService.getMenuPermission(sysMenu.getMenuId()));
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return success/false
     */
    @PostMapping("/save")
    public R save(@Valid @RequestBody SysMenu sysMenu) {
        SysMenu byId = sysMenuService.getById(sysMenu.getMenuId());
        if(byId!=null){
            return new R<>(CommonConstants.FAIL,Boolean.FALSE,String.format("菜单【%s】已存在",byId.getMenuId()));
        }
        return new R<>(sysMenuService.save(sysMenu));
    }

    /**
     * 删除菜单
     *
     * @param sysMenu 菜单ID
     * @return success/false
     */
    @PostMapping("/removeById")
    public R removeById(@RequestBody SysMenu sysMenu) {
        return sysMenuService.removeMenuById(sysMenu.getMenuId());
    }

    /**
     * 更新菜单
     *
     * @param sysMenu
     * @return
     */
    @PostMapping("/update")
    public R update(@Valid @RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.updateMenuById(sysMenu));
    }
}

