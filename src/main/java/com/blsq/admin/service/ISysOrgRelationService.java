package com.blsq.admin.service;

import com.blsq.admin.model.SysOrg;
import com.blsq.admin.model.SysOrgRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-31
 */
public interface ISysOrgRelationService extends IService<SysOrgRelation> {

    /**
     * 新建组织关系
     * @param sysOrg
     */
    void insertOrgRelation(SysOrg sysOrg);

    /**
     * 更新组织关系
     * @param relation
     */
    void updateOrgRelation(SysOrgRelation relation);


    /**
     * 通过ID删除部门关系
     * @param orgId
     */
    void deleteAllOrgRealtion(Integer orgId);



}
