package com.blsq.admin.common.vo;

import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.model.SysOrg;
import com.blsq.admin.model.SysUserOrg;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liujiayi
 * @date 2019/22/31
 */
@Data
public class UserVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private Integer userId;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 *
	 */
	private String password;
	/**
	 * 随机盐
	 */
	private String salt;

	/**
	 * 微信openid
	 */
	private String wxOpenid;

	/**
	 * QQ openid
	 */
	private String qqOpenid;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 0-正常，1-删除
	 */
	private String delFlag;

	/**
	 * 锁定标记
	 */
	private String lockFlag;
	/**
	 * 简介
	 */
	private String phone;
	/**
	 * 头像
	 */
	private String avatar;

	private String avatarAbsolute= CommonConstants.PICTURE_ADDRESS+this.avatar;
	/**
	 * 租户ID
	 */
	private Integer tenantId;

	/**
	 * 部门ID
	 */
	private Integer deptId;

	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 * 真实名称
	 */
	private String realname;
	/**
	 * 邮箱
	 */
	private String mailbox;
	/**
	 * 身份证
	 */
	private String identityCard;
	/**
	 * 角色列表
	 */
	private List<SysRoleVO> roleList;

	private SysOrg org;
}
