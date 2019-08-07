package com.blsq.admin.common.vo;


import com.blsq.admin.common.dto.MenuTree;
import com.blsq.admin.common.dto.TreeNode;
import com.blsq.admin.model.SysMenu;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liujiayi
 * @date 2019/22/31
 */
@UtilityClass
public class TreeUtil {
	/**
	 * 两层循环实现建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {

		List<T> trees = new ArrayList<>();

		for (T treeNode : treeNodes) {

			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}

			for (T it : treeNodes) {
				if (it.getParentId() == treeNode.getId()) {
					if (treeNode.getData() == null) {
						treeNode.setData(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}

	/**
	 * 使用递归方法建树
	 *
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 *
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId() == it.getParentId()) {
				if (treeNode.getData() == null) {
					treeNode.setData(new ArrayList<>());
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * 通过sysMenu创建树形节点
	 *
	 * @param menus
	 * @param root
	 * @return
	 */
	public List<MenuTree> buildTree(List<SysMenu> menus, int root) {
		List<MenuTree> trees = new ArrayList<>();
		MenuTree node;
		for (SysMenu menu : menus) {
			node = new MenuTree();
			node.setId(menu.getMenuId());
			node.setName(menu.getName());
			node.setDescription(menu.getDescription());
			node.setPermission(menu.getPermission());
			node.setParentId(menu.getParentId());
			node.setUrl(menu.getUrl());
			node.setPath(menu.getPath());
			node.setIcon(menu.getIcon());
			node.setComponent(menu.getComponent());
			node.setSort(menu.getSort());
			node.setKeepAlive(menu.getKeepAlive());
			node.setType(menu.getType());
			node.setCreateTime(menu.getCreateTime());
			node.setUpdateTime(menu.getUpdateTime());
			node.setDelFlag(menu.getDelFlag());
			trees.add(node);
		}
		return TreeUtil.build(trees, root);
	}
}
