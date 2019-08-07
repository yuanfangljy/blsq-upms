package com.blsq.admin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.constant.SecurityConstants;
import com.blsq.admin.common.dto.UserDTO;
import com.blsq.admin.common.dto.UserInfo;
import com.blsq.admin.common.dto.UserOrgRoleDTO;
import com.blsq.admin.common.dto.UserResetDTO;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.vo.*;
import com.blsq.admin.model.*;
import com.blsq.admin.mapper.SysMenuMapper;
import com.blsq.admin.mapper.SysUserMapper;
import com.blsq.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService  {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private final SysUserMapper userMapper;
    private final ISysMenuService menuService;
    private final ISysDeptService deptService;
    private final SysMenuMapper menuMapper;
    private final ISysUserOrgService sysUserOrgService;
    private final ISysOrgRoleService sysOrgRoleService;




    /**
     * 获取全部的用户信息
     *
     * @param userVO 角色名
     * @return userInfo
     */
    @Override
    public UserInfo findUserInfo(UserVO userVO) {
        //1、查询用户基本信息
        SysUser condition = new SysUser();
        condition.setUsername(userVO.getUsername());
        SysUser sysUser= userMapper.selectOne(new QueryWrapper<>(condition));

        UserInfo userInfo=new UserInfo();
        userInfo.setSysUser(sysUser);

        //2、设置角色列表
        List<String> roleCodes=userVO.getRoleList().stream()
                .filter(sysRole -> !StrUtil.equals(SecurityConstants.BASE_ROLE,sysRole.getRoleCode()))
                .map(SysRoleVO::getRoleCode)
                .collect(Collectors.toList());
        String[] roles = ArrayUtil.toArray(roleCodes, String.class);
        userInfo.setRoles(roles);


        //3、设置权限列表（menu.permission）
        Set<String> permissions=new HashSet<>();
        Arrays.stream(roles).forEach(roleCode->{
            //根据角色查询用户权限，进行迭代
            List<MenuVO> menuVOS = menuMapper.selectMenuByRoleCode(roleCode);
            List<String> permissionList = menuVOS.stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVO::getPermission).collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));

        return userInfo;
    }

    /**
     * 查询用户信息
     * @param sysUser 用户
     * @return
     */
    @Override
    public UserInfo findUserInfos(SysUser sysUser) {
        return getUserInfo(sysUser);
    }

    /**
     * 提供给内部接口：查询用户信息
     *
     * @param username 用户名
     * @return userInfo
     */
    @Override
    public R<UserInfo> findUserInfoApi(String username) {
        SysUser user = userMapper.selectOne(new QueryWrapper<SysUser>()
                .lambda().eq(SysUser::getUsername, username));

        return new R<>(getUserInfo(user));
    }

    /**
     * 根据用户Id，查询角色Id
     * @param user
     * @return
     */
    public List<Integer> getRoleIdList(SysUser user){
        //1、查询用户所包含的组织sys_user_org；
        Set<Integer> orgIds = sysUserOrgService.list(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getUserId, user.getUserId())).stream().map(SysUserOrg::getOrgId).collect(Collectors.toSet());
        //2、根据组织Id，查询角色对应的角色Id;sys_org_role
        List<Integer> roleIdList=new ArrayList<>();
        orgIds.forEach(orgId->{
            Set<Integer> roleIds = sysOrgRoleService.list(new QueryWrapper<SysOrgRole>().lambda().eq(SysOrgRole::getOrgId, orgId)).stream().map(SysOrgRole::getRoleId).collect(Collectors.toSet());
            roleIdList.addAll(roleIds);
        });
        return roleIdList;
    }

    /**
     * 获取用户信息
     * @param sysUser
     * @return
     */
    public UserInfo getUserInfo(SysUser sysUser){
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表  （ID）
        List<Integer> roleIds = getRoleIdList(sysUser);
        userInfo.setListRoles(ArrayUtil.toArray(roleIds, Integer.class));

        //设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> permissionList = menuService.findMenuByRoleId(roleId)
                    .stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVO::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        //设置url列表（menu.url）
        //设置权限列表（menu.permission）
        Set<String> url = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> urlList = menuService.findMenuByRoleId(roleId)
                    .stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getUrl()))
                    .map(MenuVO::getUrl)
                    .collect(Collectors.toList());
            url.addAll(urlList);
        });
        userInfo.setUrl(ArrayUtil.toArray(url, String.class));
        return userInfo;
    };

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @Override
    public UserVO selectUserVoById(Integer id) {
        return baseMapper.getUserVoById(id);
    }

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return Boolean
     */
    @Override
    @CacheEvict(value = "user_details", key = "#sysUser.username")
    public Boolean deleteUserById(SysUser sysUser) {
        //1、根据用户Id，查询sys_user_org中的信息中的org_id
        List<Integer> orgIdList = sysUserOrgService.list(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getUserId, sysUser.getUserId())).stream().map(SysUserOrg::getOrgId).collect(Collectors.toList());
        //2、删除组织角色表中的组织信息
        orgIdList.stream().forEach(orgId->{
            sysOrgRoleService.remove(new QueryWrapper<SysOrgRole>().lambda().eq(SysOrgRole::getOrgId,orgId));
        });
        //2、删除用户组织信息中的userId相关，再添加
        sysUserOrgService.remove(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getUserId,sysUser.getUserId()));
        //2、删除
        this.removeById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    /**
     * 保存用户信息
     *
     * @param userDto DTO 对象
     * @return success/fail
     */
    @Override
    @Transactional
    public Boolean saveUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        baseMapper.insert(sysUser);
       /* //根据组织ID，查询角色IDS
        List<Integer> roleIdList = sysOrgRoleService.list(new QueryWrapper<SysOrgRole>().lambda().eq(SysOrgRole::getOrgId, userDto.getOrgIds())).stream().map(SysOrgRole::getRoleId).collect(Collectors.toList());*/
        //更新用户组织信息
        saveUpdateOrgUser(sysUser.getUserId(),userDto.getOrgId());

       /* List<SysUserRole> userRoleList = roleIdList
                .stream().map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getUserId());
                    userRole.setRoleId(roleId);
                    return userRole;
                }).collect(Collectors.toList());*/
        return Boolean.TRUE;
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @Override
    @CacheEvict(value = "user_details", key = "#userDto.username")
    public Boolean updateUser(@Valid UserDTO userDto) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userDto.getUserId());
        sysUser.setUsername(userDto.getUsername());
        sysUser.setRealname(userDto.getRealname());
        sysUser.setPhone(userDto.getPhone());
        sysUser.setAvatar(userDto.getAvatar());
        sysUser.setMailbox(userDto.getMailbox());
        //BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());

        //更新用户组织信息
        saveUpdateOrgUser(sysUser.getUserId(),userDto.getOrgId());

        /*if (StrUtil.isNotBlank(userDto.getPassword())) {
            sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        }*/
        this.updateById(sysUser);

        /*userRoleService.remove(new QueryWrapper<SysUserRole>().lambda()
                .eq(SysUserRole::getUserId, userDto.getUserId()));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });*/
        return Boolean.TRUE;
    }

    /**
     * 新增和更新，用户组织信息表
     * @param userId
     * @param orgId
     * @return
     */
    @Transactional
    public Boolean saveUpdateOrgUser(Integer userId,Integer orgId){

        // 需要删除用户组织信息中的userId相关，再添加
        sysUserOrgService.remove(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getUserId,userId));

            SysUserOrg userOrg=new SysUserOrg();
            userOrg.setUserId(userId);
            userOrg.setOrgId(orgId);
            sysUserOrgService.save(userOrg);
        return Boolean.TRUE;
    }


    /**
     * 新增和更新，组织角色信息表
     * @param roleIds
     * @param orgIds
     * @return
     */
    @Transactional
    public Boolean saveUpdateOrgRole(List<Integer> roleIds,List<Integer> orgIds){

        // 需要删除组织角色信息中的userId相关，再添加
        orgIds.stream().forEach(orgId->{
            sysOrgRoleService.remove(new QueryWrapper<SysOrgRole>().lambda().eq(SysOrgRole::getOrgId,orgId));
        });

        orgIds.forEach(orgId->{
            roleIds.forEach(roleId->{
                SysOrgRole orgRole=new SysOrgRole();
                orgRole.setRoleId(roleId);
                orgRole.setOrgId(orgId);
                sysOrgRoleService.save(orgRole);
            });
        });

        return Boolean.TRUE;
    }

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    @Override
    public IPage getUsersWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * 更新当前用户基本信息
     *
     * @param userDto 用户信息
     * @return Boolean
     */
    @Override
    @CacheEvict(value = "user_details", key = "#userDto.username")
    public R<Boolean> updateUserInfo(@Valid UserDTO userDto) {
        SysUser userVO= baseMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, userDto.getUsername()));

        SysUser sysUser = new SysUser();
        sysUser.setUserId(userVO.getUserId());
        sysUser.setUsername(userDto.getUsername());
        sysUser.setRealname(userDto.getRealname());
        sysUser.setPhone(userDto.getPhone());
        sysUser.setAvatar(userDto.getAvatar());
        sysUser.setMailbox(userDto.getMailbox());
        return new R<>(this.updateById(sysUser));
    }


    /**
     * 重置用户密码
     * @param userResetDTO
     * @return
     */
    @Override
    @CacheEvict(value = "user_details", key = "#userResetDTO.username")
    public R<Boolean> resetUserPassWord(@Valid UserResetDTO userResetDTO) {
        SysUser userVO = baseMapper.getUserByUsername(userResetDTO.getUsername());
        if(userVO==null){
            return new R(CommonConstants.FAIL,"fail","请勿暴力操作");
        }
        SysUser sysUser = new SysUser();
        if(StrUtil.isNotBlank(userResetDTO.getNewPassWord())){
            sysUser.setPassword(ENCODER.encode(userResetDTO.getNewPassWord()));
        }
        sysUser.setUserId(userVO.getUserId());
        return new R<>(this.updateById(sysUser));
    }

    /**
     * 修改密码
     * @param userResetDTO
     * @return
     */
    @Override
    @CacheEvict(value = "user_details", key = "#userResetDTO.username")
    public R updateUserPassWord(@Valid UserResetDTO userResetDTO) {
        SysUser userVO= baseMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, userResetDTO.getUsername()));
        SysUser sysUser = new SysUser();
        if (StrUtil.isNotBlank(userResetDTO.getPassWord())
                && StrUtil.isNotBlank(userResetDTO.getNewPassWord()) && StrUtil.isNotBlank(userResetDTO.getJudgeNewPassWord())) {
            if (ENCODER.matches(userResetDTO.getPassWord(), userVO.getPassword())) {
                sysUser.setUserId(userVO.getUserId());
                sysUser.setPassword(ENCODER.encode(userResetDTO.getNewPassWord()));
                if(!userResetDTO.getNewPassWord().equals(userResetDTO.getJudgeNewPassWord())){
                    log.warn("重新新密码不正确", userResetDTO.getUsername());
                    return new R<>(Boolean.FALSE, "重新新密码不正确");
                }
            } else {
                log.warn("原密码错误，修改密码失败:{}", userResetDTO.getUsername());
                return new R<>(Boolean.FALSE, "原密码错误，修改失败");
            }
        }else{
            return new R<>(Boolean.FALSE, "请将输入框输入完整，再提交");
        }
        return new R<>(this.updateById(sysUser));
    }


    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R
     */
    @Override
    public List<SysUser> listAncestorUsers(String username) {
        SysUser sysUser = this.getOne(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, username));

        SysDept sysDept = deptService.getById(sysUser.getDeptId());
        if (sysDept == null) {
            return null;
        }

        Integer parentId = sysDept.getParentId();
        return this.list(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getDeptId, parentId));
    }

    /**
     * 根据组织Id，查询所有用户信息
     * @param page
     * @param userOrgPageVO
     * @return
     */
    @Override
    public List<UserVO> getUserWithOrgPage(Page page, UserOrgPageVO userOrgPageVO) {
        //1、在组织用户表中，根据orgId查询所有userId
        List<Integer> userList = sysUserOrgService.list(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getOrgId, userOrgPageVO.getOrgId())).stream().map(SysUserOrg::getUserId).collect(Collectors.toList());
        //?????
        Set<Integer> userIds=new HashSet<>();
        userIds.addAll(userList);
        //2、根据userId，批量查询用户组织及角色相关信息
        List<UserVO> userVOList=new ArrayList<>();
        userIds.forEach(userId->{
            UserVO userVO = userMapper.selectUserVoByUserId(userId);
            userVOList.add(userVO);
        });

        return userVOList;
    }

    /**
     * 根据组织ID,查询组织本级及下级的所有用户信息，角色ID
     * @param page
     * @param userOrgRoleDTO
     * @return
     */
    @Override
    public IPage getUsersWithOrgPage(Page page, UserOrgRoleDTO userOrgRoleDTO) {
        return baseMapper.setUsersWithOrgPage(page,userOrgRoleDTO);
    }



}
