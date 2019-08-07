package com.blsq.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.common.dto.DeptDTO;
import com.blsq.admin.common.dto.DeptTree;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.common.vo.TreeUtil;
import com.blsq.admin.model.SysDept;
import com.blsq.admin.mapper.SysDeptMapper;
import com.blsq.admin.model.SysDeptHouse;
import com.blsq.admin.model.SysDeptRelation;
import com.blsq.admin.service.ISysDeptHouseService;
import com.blsq.admin.service.ISysDeptRelationService;
import com.blsq.admin.service.ISysDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    private final ISysDeptRelationService sysDeptRelationService;
    private final ISysDeptHouseService sysDeptHouseService;


    /**
     * 添加信息部门
     *
     * @param deptDTO 部门
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDept(DeptDTO deptDTO) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(deptDTO, sysDept);
        this.save(sysDept);
        sysDeptRelationService.insertDeptRelation(sysDept);
        //添加部门区域
        sysDeptHouseService.saveDeptHouse(deptDTO.getDeptId(),deptDTO.getHouseIds());
        return Boolean.TRUE;
    }


    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Integer id) {
        //级联删除部门
        List<Integer> idList = sysDeptRelationService
                .list(new QueryWrapper<SysDeptRelation>().lambda()
                        .eq(SysDeptRelation::getAncestor, id))
                .stream()
                .map(SysDeptRelation::getDescendant)
                .collect(Collectors.toList());

        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }

        //删除部门级联关系
        sysDeptRelationService.deleteAllDeptRealtion(id);

        //删除部门区域关系
        sysDeptHouseService.remove(new QueryWrapper<SysDeptHouse>().lambda().eq(SysDeptHouse::getDeptId,id));
        return Boolean.TRUE;
    }

    /**
     * 更新部门
     *
     * @param deptDTO 部门信息
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDeptById(DeptDTO deptDTO) {
        //更新部门状态
        this.updateById(deptDTO);
        //更新部门关系
        SysDeptRelation relation = new SysDeptRelation();
        relation.setAncestor(deptDTO.getParentId());
        relation.setDescendant(deptDTO.getDeptId());
        sysDeptRelationService.updateDeptRealtion(relation);
        //添加部门区域
        sysDeptHouseService.saveDeptHouse(deptDTO.getDeptId(),deptDTO.getHouseIds());
        return Boolean.TRUE;
    }

    /**
     * 查询全部部门树
     *
     * @return 树
     */
    @Override
    public List<DeptTree> selectTree() {
        return getDeptTree(this.list(new QueryWrapper<>()));
    }

    /**
     * 查询用户部门树
     *
     * @return
     */
    @Override
    public List<DeptTree> getUserTree() {
        Integer deptId =  SecurityUtils.getUser().getDeptId();
        List<Integer> descendantIdList = sysDeptRelationService
                .list(new QueryWrapper<SysDeptRelation>().lambda()
                        .eq(SysDeptRelation::getAncestor, deptId))
                .stream().map(SysDeptRelation::getDescendant)
                .collect(Collectors.toList());

        List<SysDept> deptList = baseMapper.selectBatchIds(descendantIdList);
        return getDeptTree(deptList);
    }

    /**
     * 构建部门树
     *
     * @param depts 部门
     * @return
     */
    private List<DeptTree> getDeptTree(List<SysDept> depts) {
        List<DeptTree> treeList = depts.stream()
                .filter(dept -> !dept.getDeptId().equals(dept.getParentId()))
                .map(dept -> {
                    DeptTree node = new DeptTree();
                    node.setId(dept.getDeptId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, 0);
    }
}
