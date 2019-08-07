package com.blsq.admin.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.constant.SecurityConstants;
import com.blsq.admin.common.dto.UserInfo;
import com.blsq.admin.common.util.R;
import com.blsq.admin.mapper.SysUserMapper;
import com.blsq.admin.model.SysRole;
import com.blsq.admin.model.SysUser;
import com.blsq.admin.model.SysUserOrg;
import com.blsq.admin.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 类描述：  用户详细信息
 * </p>
 *
 * @author liujiayi
 * @since 2019/4/18 11:24
 * @version：1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class BlsqUserDetailsServiceImpl implements BlsqUserDetailsService{

    private final CacheManager cacheManager;
    private final ISysUserService userService;
    private final ISysUserOrgService userOrgService;
    private final ISysOrgService sysOrgService;

    /**
     * Security 进行登录授权：根据用户名查询用户信息
     * @param username  用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Cache cache = cacheManager.getCache("user_details");
        if (cache != null && cache.get(username) != null) {
            return (BlsqUser) cache.get(username).get();
        }
        R<UserInfo> result = userService.findUserInfoApi(username);
        UserDetails userDetails = getUserDetailsApi(result);
        //将用户信息存放到redis中
        cache.put(username, userDetails);
        //将当前用户小区存放到redis中
        sysOrgService.getHouseIdsByOrgList(username);
        return userDetails;

        //查询用户信息
      /* UserVO userVo = userMapper.selectUserVoByUserName(username);
        if (userVo == null) {
            throw new UsernameNotFoundException("用户名不存在或者密码错误");
        }
        return new UserDetailsImpl(userVo);*/
    }


    /**
     * 构建userdetails
     *
     * @param result 用户信息
     * @return
     */
    private UserDetails getUserDetailsApi(R<UserInfo> result) {
        if (result == null || result.getData() == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        UserInfo info = result.getData();

        Set<String> dbAuthsSet = new LinkedHashSet<>();

        if (ArrayUtil.isNotEmpty(info.getListRoles())) {
            // 获取角色
            Arrays.stream(info.getListRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
            //权限
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
        }

        SysUser user = info.getSysUser();
        //组织ID[]
        List<Integer> sysUserOrgList = userOrgService.list(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getUserId, user.getUserId())).stream().map(SysUserOrg::getOrgId).collect(Collectors.toList());
        Integer[] orgIds = sysUserOrgList.stream().toArray(Integer[]::new);

        //保存角色权限
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));

        boolean enabled = StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL);
        // 构造security用户
        return new BlsqUser(user.getUserId(),orgIds ,user.getDeptId(), user.getTenantId(), user.getUsername(),  user.getPassword(), enabled,
                true, true, !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()), authorities);
    }





    /**
     * 构建userdetails
     *
     * @param result 用户信息
     * @return
     */
    private UserDetails getUserDetails(R<UserInfo> result) {
        if (result == null || result.getData() == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        UserInfo info = result.getData();

        Set<String> dbAuthsSet = new HashSet<>();
        if (ArrayUtil.isNotEmpty(info.getListRoles())) {
            // 获取角色
            Arrays.stream(info.getListRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

        }
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user = info.getSysUser();
        List<Integer> sysUserOrgList = userOrgService.list(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getUserId, user.getUserId())).stream().map(SysUserOrg::getOrgId).collect(Collectors.toList());
        Integer[] orgIds = sysUserOrgList.stream().toArray(Integer[]::new);

        boolean enabled = StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL);
        // 构造security用户
        return new BlsqUser(user.getUserId(), orgIds,user.getDeptId(), user.getTenantId(), user.getUsername(), SecurityConstants.BCRYPT + user.getPassword(), enabled,
                true, true, !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()), authorities);
    }
}
