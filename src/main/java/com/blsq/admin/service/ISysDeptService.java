package com.blsq.admin.service;

import com.blsq.admin.common.dto.DeptDTO;
import com.blsq.admin.common.dto.DeptTree;
import com.blsq.admin.model.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 查询部门树菜单
     *
     * @return 树
     */
    List<DeptTree> selectTree();

    /**
     * 查询用户部门树
     *
     * @return
     */
    List<DeptTree> getUserTree();

    /**
     * 添加信息部门
     *
     * @param deptDTO
     * @return
     */
    Boolean saveDept(DeptDTO deptDTO);

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    Boolean removeDeptById(Integer id);

    /**
     * 更新部门
     *
     * @param deptDTO 部门信息
     * @return 成功、失败
     */
    Boolean updateDeptById(DeptDTO deptDTO);
}
