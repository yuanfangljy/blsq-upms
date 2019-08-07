package com.blsq.admin.common.dto;

import com.blsq.admin.common.vo.MenuVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author liujiayi
 * @date 2019/22/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode {
	private String icon;
	private String name;
	private boolean spread = false;
	private String path;
	private String component;
	private String authority;
	private String redirect;
	private String keepAlive;
	private String code;
	private String type;
	private String label;
	private Integer sort;
	private String description;
	private String permission;
	private String url;
	private int parentId;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private String delFlag;

	public MenuTree() {
	}

	public MenuTree(int id, String name, int parentId) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.label = name;
	}

	public MenuTree(int id, String name, MenuTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.name = name;
		this.label = name;
	}

	public MenuTree(MenuVO menuVo) {
		this.id = menuVo.getMenuId();
		this.name = menuVo.getName();
		this.description=menuVo.getDescription();
		this.permission=menuVo.getPermission();
		this.url=menuVo.getUrl();
		this.path = menuVo.getPath();
		this.parentId = menuVo.getParentId();
		this.icon = menuVo.getIcon();
		this.component = menuVo.getComponent();
		this.sort = menuVo.getSort();
		this.keepAlive = menuVo.getKeepAlive();
		this.type = menuVo.getType();
		this.label = menuVo.getName();
		this.delFlag=menuVo.getDelFlag();
		this.createTime=menuVo.getCreateTime();
		this.updateTime=menuVo.getUpdateTime();
	}
}
