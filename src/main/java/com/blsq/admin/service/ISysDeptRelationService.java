package com.blsq.admin.service;

import com.blsq.admin.model.SysDept;
import com.blsq.admin.model.SysDeptRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 部门关系表 服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface ISysDeptRelationService extends IService<SysDeptRelation> {

    /**
     * 新建部门关系
     *
     * @param sysDept 部门
     */
    void insertDeptRelation(SysDept sysDept);

    /**
     * 通过ID删除部门关系
     *
     * @param id
     */
    void deleteAllDeptRealtion(Integer id);

    /**
     * 更新部门关系
     *
     * @param relation
     */
    void updateDeptRealtion(SysDeptRelation relation);
}
