package com.blsq.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.util.R;
import com.blsq.admin.mapper.SysOrgMapper;
import com.blsq.admin.model.SysOrg;
import com.blsq.admin.model.SysOrgHouse;
import com.blsq.admin.model.SysOrgRole;
import com.blsq.admin.mapper.SysOrgRoleMapper;
import com.blsq.admin.service.ISysOrgRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blsq.admin.service.ISysOrgService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-30
 */
@Service
@AllArgsConstructor
public class SysOrgRoleServiceImpl extends ServiceImpl<SysOrgRoleMapper, SysOrgRole> implements ISysOrgRoleService {

    private SysOrgMapper sysOrgMapper;

    /**
     * 保存、更新组织角色表
     * @param orgId
     * @param roleIds
     * @return
     */
    @Override
    @Transactional
    public R saveUpdateOrgRole(Integer orgId, List<Integer> roleIds) {
        //1、查询当前结点是否有子节点，如果有则不能删除
        deleteOrgRole(orgId);
        //2、先删除，orgId相关角色信息
        this.remove(new QueryWrapper<SysOrgRole>().lambda().eq(SysOrgRole::getOrgId,orgId));
        //3、再进行保存
        roleIds.forEach(roleId->{
            SysOrgRole orgRole=new SysOrgRole();
            orgRole.setRoleId(roleId);
            orgRole.setOrgId(orgId);
            this.save(orgRole);
        });
        return new R<>();
    }

    /**
     * 删除组织角色信息
     * @param orgId
     * @return
     */
    @Override
    public R deleteOrgRole(Integer orgId) {
        List<SysOrg> orgList = sysOrgMapper.selectOrgByParentId(orgId);
        if (CollUtil.isNotEmpty(orgList)) {
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("组织含有下级不能删除").build();
        }
        return new R<>();
    }


}
