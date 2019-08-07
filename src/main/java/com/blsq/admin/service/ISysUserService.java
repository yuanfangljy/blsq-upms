package com.blsq.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blsq.admin.common.dto.UserDTO;
import com.blsq.admin.common.dto.UserInfo;
import com.blsq.admin.common.dto.UserOrgRoleDTO;
import com.blsq.admin.common.dto.UserResetDTO;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.vo.UserOrgPageVO;
import com.blsq.admin.common.vo.UserVO;
import com.blsq.admin.model.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 获取全部的用户信息
     * @param userVO 用户
     * @return userInfo
     */
    UserInfo findUserInfo(UserVO userVO);

    /**
     * 查询用户信息
     *
     * @param sysUser 用户
     * @return userInfo
     */
    UserInfo findUserInfos(SysUser sysUser);

    /**
     * 提供给内部接口：查询用户信息
     *
     * @param userName 用户名
     * @return userInfo
     */
    R<UserInfo> findUserInfoApi(String userName);

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    UserVO selectUserVoById(Integer id);

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return boolean
     */
    Boolean deleteUserById(SysUser sysUser);

    /**
     * 保存用户信息
     *
     * @param userDto DTO 对象
     * @return success/fail
     */
    Boolean saveUser(UserDTO userDto);

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    Boolean updateUser(@Valid UserDTO userDto);

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    IPage getUsersWithRolePage(Page page, UserDTO userDTO);

    /**
     * 更新当前用户基本信息
     *
     * @param userDto 用户信息
     * @return Boolean
     */
    R<Boolean> updateUserInfo(@Valid UserDTO userDto);

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R
     */
    List<SysUser> listAncestorUsers(String username);

    /**
     * 根据组织Id，查询所有员工信息
     * @param page
     * @param userOrgPageVO
     * @return
     */
    List<UserVO> getUserWithOrgPage(Page page, UserOrgPageVO userOrgPageVO);

    /**
     * 根据组织ID,查询组织本级及下级的所有用户信息，角色ID
     * @param page
     * @param userOrgRoleDTO
     * @return
     */
    IPage getUsersWithOrgPage(Page page, UserOrgRoleDTO userOrgRoleDTO);

    /**
     * 重置用户密码
     * @param userResetDTO
     * @return
     */
    R<Boolean> resetUserPassWord(@Valid UserResetDTO userResetDTO);

    /**
     * 修改用户密码
     * @param userResetDTO
     * @return
     */
    R updateUserPassWord(@Valid UserResetDTO userResetDTO);
}
