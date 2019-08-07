package com.blsq.admin.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liujiayi
 * @date 2019/22/31
 */
@Data
public class TreeNode {
	protected int id;
	protected int parentId;
	protected List<TreeNode> data = new ArrayList<TreeNode>();

	public void add(TreeNode node) {
		data.add(node);
	}
}
