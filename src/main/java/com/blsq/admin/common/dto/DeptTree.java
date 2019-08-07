package com.blsq.admin.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiayi
 * @date 2019/22/31
 * 部门树
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode {
	private String name;
}
