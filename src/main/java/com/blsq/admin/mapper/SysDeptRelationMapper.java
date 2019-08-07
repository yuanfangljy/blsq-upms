package com.blsq.admin.mapper;

import com.blsq.admin.model.SysDeptRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 部门关系表 Mapper 接口
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public interface SysDeptRelationMapper extends BaseMapper<SysDeptRelation> {

    /**
     * 删除部门关系表数据
     *
     * @param id 部门ID
     */
    void deleteDeptRelationsById(Integer id);

    /**
     * 更改部分关系表数据
     *
     * @param deptRelation
     */
    void updateDeptRelations(SysDeptRelation deptRelation);
}
