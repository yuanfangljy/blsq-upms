package com.blsq.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blsq.admin.common.data.datascope.DataScope;
import com.blsq.admin.common.dto.UserDTO;
import com.blsq.admin.common.dto.UserOrgRoleDTO;
import com.blsq.admin.common.vo.UserOrgPageVO;
import com.blsq.admin.common.vo.UserOrgRoleVO;
import com.blsq.admin.common.vo.UserVO;
import com.blsq.admin.model.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username 用户名
     * @return userVo
     */
    UserVO selectUserVoByUserName(@Param("username") String username);

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    UserVO getUserVoById(Integer id);

    /**
     * 分页查询用户信息（含角色）
     *
     * @param page      分页
     * @param userDTO   查询参数
     * @param
     * @return list
     */
    IPage<List<UserVO>> getUserVosPage(Page page, @Param("query") UserDTO userDTO);

    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username 用户名
     * @return userVo
     */
    UserVO getUserVoByUsername(String username);

    /**
     * 根据userId查询，相关用户信息
     * @param userId
     * @return
     */
    UserVO selectUserVoByUserId(@Param("userId") Integer userId);

    /**
     * 根据组织ID,查询组织本级及下级的所有用户信息，角色ID
     * @param page
     * @param userOrgRoleDTO
     * @return
     */
    IPage setUsersWithOrgPage(Page page, @Param("userOrgRoleDTO") UserOrgRoleDTO userOrgRoleDTO);

    /**
     * 通过用户名查询用户信息
     * @param username
     * @return
     */
    SysUser getUserByUsername(@Param("username") String username);
}
