package com.blsq.admin.common.dto;

import com.blsq.admin.model.SysUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liujiayi
 * @date 2019/22/31
 * <p>
 * commit('SET_ROLES', data)
 * commit('SET_NAME', data)
 * commit('SET_AVATAR', data)
 * commit('SET_INTRODUCTION', data)
 * commit('SET_PERMISSIONS', data)
 */
@Data
public class UserInfo implements Serializable {
	/**
	 * 用户基本信息
	 */
	private SysUser sysUser;
	/**
	 * 权限标识集合
	 */
	private String[] permissions;

	/**
	 * 请求url
	 */
	private String[] url;

	/**
	 * 角色集合
	 */
	private String[] roles;

	/**
	 * 角色集合
	 */
	private Integer[] listRoles;
}
