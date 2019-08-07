package com.blsq.admin.common.security;

import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.constant.SecurityConstants;
import com.blsq.admin.common.security.service.BlsqUser;
import com.blsq.admin.common.vo.PermissionVO;
import com.blsq.admin.common.vo.SysRoleVO;
import com.blsq.admin.common.vo.UserVO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * <p>
 * 类描述：
 * </p>
 *
 * @author liujiayi
 * @version：1.0
 * @since 2018/12/21 16:48
 */
@Slf4j
public class UserDetailsImpl implements UserDetails {


    @Getter
    private Integer userId;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private String status;
    @Getter
    private Integer deptId;
    @Getter
    private Integer tenantId;
    @Getter
    private String lockFlag;
    @Getter
    private List<PermissionVO> permissionVOList;
    @Getter
    private List<SysRoleVO> roleList;

    public UserDetailsImpl(UserVO userVo){
        this.userId=userVo.getUserId();
        this.username = userVo.getUsername();
        this.password = userVo.getPassword();
        this.status = userVo.getLockFlag();
        this.roleList = userVo.getRoleList();
        this.deptId = userVo.getDeptId();
        this.lockFlag = userVo.getLockFlag();
        this.tenantId = userVo.getTenantId();
    }

    /**
     * (Collection<? extends GrantedAuthority>) new BlsqUser(this.userId, this.deptId, this.tenantId, this.username, SecurityConstants.BCRYPT + this.password, false,
     true, true, !CommonConstants.STATUS_LOCK.equals(this.lockFlag), authorityList)
     * @return
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        // 根据用户查询用户对应权限,树状结构
        for (SysRoleVO permission : roleList) {
            //log.info("---- 权限 ----"+permission.toString());
            authorityList.add(new SimpleGrantedAuthority(permission.getRoleCode()));
        }
        authorityList.add(new SimpleGrantedAuthority(SecurityConstants.BASE_ROLE));
        return authorityList;
    }

    /**
     * 数据库中密码
     * @return
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    //判断帐号是否已经过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //锁定用户  9
    @Override
    public boolean isAccountNonLocked() {
        return !StringUtils.equals(CommonConstants.STATUS_LOCK, status);
    }

    //判断用户凭证是否已经过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //正常用户  1
    @Override
    public boolean isEnabled() {
        return StringUtils.equals(CommonConstants.STATUS_NORMAL, status);
    }
}
