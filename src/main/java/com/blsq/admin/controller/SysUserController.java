package com.blsq.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.dto.UserDTO;
import com.blsq.admin.common.dto.UserOrgRoleDTO;
import com.blsq.admin.common.dto.UserResetDTO;
import com.blsq.admin.common.util.CommonPageResponse;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.common.vo.UserOrgPageVO;
import com.blsq.admin.common.vo.UserOrgRoleVO;
import com.blsq.admin.common.vo.UserVO;
import com.blsq.admin.model.SysUser;
import com.blsq.admin.service.ISysUserService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@RestController
@RequestMapping("/sysUser")
@AllArgsConstructor
@Slf4j
@Api(value = "user", description = "用户管理模块")
public class SysUserController {

    private final ISysUserService userService;

    @PostMapping("/login")
    public void login(){
        log.debug("---666---进入到登录访问------");
    }


    /**
     * 根据当前用户的组织Id，查询组织一下的全部用户信息
     * 当Id为空时，查询该组织下，全部的用户信息
     *
     * @return
     */
    @PostMapping(value = {"/userOrgList"})
    public R getUserWithOrgPage(Page page, UserOrgPageVO userOrgPageVO){
        List<UserVO> userWithOrgPage=  userService.getUserWithOrgPage(page,userOrgPageVO);
        return new R<>(userWithOrgPage);
    }

    /**
     * 根据组织ID,查询组织本级及下级的所有用户信息，角色ID
     *
     * @return
     */
    @PostMapping(value = {"/getUserPageByOrgId"})
    public R getUserPageByOrgId(Page page, UserOrgRoleDTO userOrgRoleDTO){
        IPage userOrgPage= userService.getUsersWithOrgPage(page,userOrgRoleDTO);
        CommonPageResponse commonPageResponse = CommonPageResponse.makeSuccessResp(userOrgPage);
        return commonPageResponse;
    }



    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @PostMapping(value = {"/userInfo"})
    public R info() {
        String username = SecurityUtils.getUser().getUsername();
        SysUser user = userService.getOne(new QueryWrapper<SysUser>()
                .lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return new R<>(Boolean.FALSE, "获取当前用户信息失败");
        }
        return new R<>(userService.findUserInfos(user));
    }

    /**
     * 获取指定用户全部信息
     * sysUser
     * @return 用OST户信息
     */
    @PostMapping("/info")
    public R info(@RequestBody SysUser sysUser) {
        SysUser user = userService.getOne(new QueryWrapper<SysUser>()
                .lambda().eq(SysUser::getUsername, sysUser.getUsername()));
        if (user == null) {
            return new R<>(Boolean.FALSE, String.format("用户信息为空 %s", sysUser.getUsername()));
        }
        return new R<>(userService.findUserInfos(user));
    }

    /**
     * 通过ID查询用户信息
     *
     * @param sysUser
     * @return 用户信息
     */
    @PostMapping("/user")
    public R user(@RequestBody SysUser sysUser) {
        UserVO userVO = userService.selectUserVoById(sysUser.getUserId());
        userVO.setAvatarAbsolute(CommonConstants.PICTURE_ADDRESS+userVO.getAvatar());
        return new R<>(userVO);
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param sysUser 用户信息
     * @return
     */
    @PostMapping("/userDetails")
    public R userDetails(@RequestBody SysUser sysUser) {
        SysUser condition = new SysUser();
        condition.setUsername(sysUser.getUsername());
        return new R<>(userService.getOne(new QueryWrapper<>(condition)));
    }

    /**
     * 删除用户信息
     *
     * @param user
     * @return R
     */
    @PostMapping("/userDel")
    public R userDel(@RequestBody SysUser user) {
        SysUser sysUser = userService.getById(user.getUserId());
        return new R<>(userService.deleteUserById(sysUser));
    }


    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @PostMapping("/addUser")
    public R addUser(@Valid @RequestBody UserDTO userDto) {
        //1、判断用户名是否存在
        SysUser existUser = userService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername,userDto.getUsername()));
        if(existUser!=null){
            return R.builder().code(CommonConstants.FAIL).message("用户名已存在，请重新输入").build();
        }
        return new R<>(userService.saveUser(userDto));
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @PostMapping("/updateUser")
    public R updateUser(@Valid @RequestBody UserDTO userDto) {
        SysUser byId = userService.getById(userDto.getUserId());
        if(!userDto.getUsername().equals(byId.getUsername())){
            //1、判断用户名是否存在
            SysUser existUser = userService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername,userDto.getUsername()));
            if(existUser!=null){
                return R.builder().code(CommonConstants.FAIL).message("用户名已存在，请重新输入").build();
            }
        }
        return new R<>(userService.updateUser(userDto));
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return 用户集合
     */
    @PostMapping("/page")
    public R getUserPage(Page page, UserDTO userDTO) {
        IPage usersWithRolePage = userService.getUsersWithRolePage(page, userDTO);
        CommonPageResponse commonPageResponse = CommonPageResponse.makeSuccessResp(usersWithRolePage);
        return commonPageResponse;

    }

    /**
     * 重置密码
     * @param userResetDTO
     * @return
     */
    @PostMapping("/resetUserPassWord")
    public R resetUserPassWord(@Valid @RequestBody UserResetDTO userResetDTO){
        return userService.resetUserPassWord(userResetDTO);
    }

    /**
     * 修改个人密码
     * @return
     */
    @PostMapping("/updateUserPassWord")
    public R updateUserPassWord(@Valid @RequestBody UserResetDTO userResetDTO){
        return userService.updateUserPassWord(userResetDTO);
    }

    /**
     * 修改个人信息
     *
     * @param userDto userDto
     * @return success/false
     */
    @PostMapping("/edit")
    public R updateUserInfo(@Valid @RequestBody UserDTO userDto) {
        return userService.updateUserInfo(userDto);
    }

    /**
     * 上级部门用户列表
     * @param sysUser 用户名称
     * @return
     */
    @PostMapping("/ancestor")
    public R listAncestorUsers(@RequestBody SysUser sysUser) {
        return new R<>(userService.listAncestorUsers(sysUser.getUsername()));
    }
}

