package com.blsq.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.dto.RoleMenusDTO;
import com.blsq.admin.common.util.CommonPageResponse;
import com.blsq.admin.common.util.R;
import com.blsq.admin.model.SysRole;
import com.blsq.admin.service.ISysRoleMenuService;
import com.blsq.admin.service.ISysRoleService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@RestController
@RequestMapping("/sysRole")
@AllArgsConstructor
@Api(value = "role", description = "角色管理模块")
public class SysRoleController {
    private final ISysRoleService sysRoleService;
    private final ISysRoleMenuService sysRoleMenuService;



    /**
     * 通过ID查询角色信息
     *
     * @param sysRole
     * @return 角色信息
     */
    @PostMapping("/getById")
    public R getById(@RequestBody SysRole sysRole) {
        return new R<>(sysRoleService.getById(sysRole.getRoleId()));
    }

    /**
     * 添加角色
     *
     * @param sysRole 角色信息
     * @return success、false
     */
    @PostMapping("/save")
    public R save(@Valid @RequestBody SysRole sysRole) {
        List<SysRole> roleCodeList = sysRoleService.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, sysRole.getRoleCode()));
        if(roleCodeList.size()>0){
            return R.builder().code(CommonConstants.FAIL).message(String.format("角色编码【%s】已存在",sysRole.getRoleCode())).build();
        }
        List<SysRole> roleNameList = sysRoleService.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleName, sysRole.getRoleName()));
        if(roleNameList.size()>0){
            return R.builder().code(CommonConstants.FAIL).message(String.format("角色名称【%s】已存在",sysRole.getRoleName())).build();
        }
        return new R<>(sysRoleService.save(sysRole));
    }

    /**
     * 修改角色
     *
     * @param sysRole 角色信息
     * @return success/false
     */
    @PostMapping("/update")
    public R update(@Valid @RequestBody SysRole sysRole) {
        SysRole byId = sysRoleService.getById(sysRole.getRoleId());
        if(byId==null){
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("操作信息不存在")
                    .build();
        }
        R r = sysRoleService.isRole(sysRole);
        if(r.getCode()==CommonConstants.FAIL){
            return r;
        }
        return new R<>(sysRoleService.updateById(sysRole));
    }

    /**
     * 删除角色
     *
     * @param sysRole
     * @return
     */
    @PostMapping("/removeById")
    public R removeById(@RequestBody SysRole sysRole) {
        return new R<>(sysRoleService.removeRoleById(sysRole.getRoleId()));
    }

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    @PostMapping("/listRoles")
    public R listRoles() {
        return new R<>(sysRoleService.list(new QueryWrapper<>()));
    }

    /**
     * 分页查询角色信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    public R getRolePage(Page page,SysRole sysRole) {
        IPage rolePage = sysRoleService.pageMaps(page, new QueryWrapper<SysRole>().like("role_name", sysRole.getRoleName()));
        CommonPageResponse commonPageResponse = CommonPageResponse.makeSuccessResp(rolePage);
        return commonPageResponse;

    }

    /**
     * 更新角色菜单(分配权限)
     * roleId  角色ID
     * menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return success、false
     */
    @PostMapping("/saveRoleMenus")
    public R saveRoleMenus(@RequestBody RoleMenusDTO roleMenusDTO) {
        SysRole sysRole = sysRoleService.getById(roleMenusDTO.getRoleId());
        return new R<>(sysRoleMenuService.saveRoleMenus(sysRole.getRoleCode(), roleMenusDTO.getRoleId(), roleMenusDTO.getMenuIds()));
    }
}

