package com.blsq.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.model.SysDept;
import com.blsq.admin.model.SysDeptRelation;
import com.blsq.admin.model.SysOrg;
import com.blsq.admin.model.SysOrgRelation;
import com.blsq.admin.mapper.SysOrgRelationMapper;
import com.blsq.admin.service.ISysOrgRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-31
 */
@Service
@AllArgsConstructor
public class SysOrgRelationServiceImpl extends ServiceImpl<SysOrgRelationMapper, SysOrgRelation> implements ISysOrgRelationService {

    private final SysOrgRelationMapper sysOrgRelationMapper;
    /**
     * 新建组织关系
     * @param sysOrg  组织
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOrgRelation(SysOrg sysOrg) {
        //新增组织关系表
        List<SysOrgRelation> relationList = sysOrgRelationMapper.selectList(new QueryWrapper<SysOrgRelation>().lambda()
                .eq(SysOrgRelation::getDescendant, sysOrg.getParentId()))
                .stream().map(relation -> {
                    relation.setDescendant(sysOrg.getOrgId());
                    return relation;
                }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(relationList)){
            this.saveBatch(relationList);
        }

        //自己也要维护到关系表中
        SysOrgRelation own=new SysOrgRelation();
        own.setDescendant(sysOrg.getOrgId());
        own.setAncestor(sysOrg.getOrgId());
        sysOrgRelationMapper.insert(own);
    }

    /**
     * 更新组织关系表
     *
     * @param relation
     */
    @Override
    public void updateOrgRelation(SysOrgRelation relation) {
        baseMapper.updateOrgRelations(relation);
    }

    /**
     * 通过ID删除部门关系
     * @param orgId
     */
    @Override
    public void deleteAllOrgRealtion(Integer orgId) {
        baseMapper.deleteOrgRelationsById(orgId);
    }


}
