package com.blsq.admin.mapper;

import com.blsq.admin.model.SysOrgRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-31
 */
public interface SysOrgRelationMapper extends BaseMapper<SysOrgRelation> {

    /**
     * 更改组织关系表数据
     * @param relation
     */
    void updateOrgRelations(SysOrgRelation relation);

    /**
     * 删除组织关系表数据
     * @param orgId
     */
    void deleteOrgRelationsById(Integer orgId);
}
