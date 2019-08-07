package com.blsq.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.model.SysDept;
import com.blsq.admin.model.SysDeptRelation;
import com.blsq.admin.mapper.SysDeptRelationMapper;
import com.blsq.admin.service.ISysDeptRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门关系表 服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@Service
@AllArgsConstructor
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation> implements ISysDeptRelationService {

    private final SysDeptRelationMapper sysDeptRelationMapper;

    /**
     * 维护部门关系
     *
     * @param sysDept 部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertDeptRelation(SysDept sysDept) {
        //增加部门关系表
        SysDeptRelation condition = new SysDeptRelation();
        condition.setDescendant(sysDept.getParentId());
        List<SysDeptRelation> relationList = sysDeptRelationMapper
                .selectList(new QueryWrapper<SysDeptRelation>().lambda()
                        .eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
                .stream().map(relation -> {
                    relation.setDescendant(sysDept.getDeptId());
                    return relation;
                }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(relationList)) {
            this.saveBatch(relationList);
        }

        //自己也要维护到关系表中
        SysDeptRelation own = new SysDeptRelation();
        own.setDescendant(sysDept.getDeptId());
        own.setAncestor(sysDept.getDeptId());
        sysDeptRelationMapper.insert(own);
    }

    /**
     * 通过ID删除部门关系
     *
     * @param id
     */
    @Override
    public void deleteAllDeptRealtion(Integer id) {
        baseMapper.deleteDeptRelationsById(id);
    }

    /**
     * 更新部门关系
     *
     * @param relation
     */
    @Override
    public void updateDeptRealtion(SysDeptRelation relation) {
        baseMapper.updateDeptRelations(relation);
    }
}
