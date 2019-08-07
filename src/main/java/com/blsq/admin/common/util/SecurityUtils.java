package com.blsq.admin.common.util;


import cn.hutool.core.util.StrUtil;
import com.blsq.admin.common.constant.SecurityConstants;
import com.blsq.admin.common.security.service.BlsqUser;
import com.blsq.admin.mapper.SysRoleMapper;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

/**
 * 安全工具类
 *
 * @author liujiayi
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 *
	 * @param authentication
	 * @return BlsqUser
	 * <p>
	 * 获取当前用户的全部信息 EnablePigxResourceServer true
	 * 获取当前用户的用户名 EnablePigxResourceServer false
	 */
	public BlsqUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof BlsqUser) {
			return (BlsqUser) principal;
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public BlsqUser getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}

	/**
	 * 获取用户角色信息
	 *
	 * @return 角色集合
	 */
	public List<Integer> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Integer> roleIds = new ArrayList<>();

		authorities.stream()
				.filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
				.forEach(granted -> {
					String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
					roleIds.add(Integer.parseInt(id));
				});
		return roleIds;
	}

}
