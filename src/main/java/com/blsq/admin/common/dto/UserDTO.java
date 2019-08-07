package com.blsq.admin.common.dto;

import com.blsq.admin.model.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author liujiayi
 * @date 2019/22/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {
	/**
	 * 角色ID
	 */
	//private List<Integer> role;

	/**
	 * 部门Id
	 */
	private Integer deptId;

	/**
	 * 组织Id
	 */
	private Integer orgId;

	/**
	 * 新密码
	 */
	private String newpassword1;
}
