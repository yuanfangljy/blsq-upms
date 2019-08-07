package com.blsq.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.mapper.SysRoleMenuMapper;
import com.blsq.admin.model.SysRole;
import com.blsq.admin.mapper.SysRoleMapper;
import com.blsq.admin.model.SysRoleMenu;
import com.blsq.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> findRolesByUserId(Integer userId) {
        return baseMapper.listRolesByUserId(userId);
    }

    /**
     * 通过角色ID，删除角色,并清空角色菜单缓存
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(value = "menu_details", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRoleById(Integer id) {
        sysRoleMenuMapper.delete(new UpdateWrapper<SysRoleMenu>()
                .lambda()
                .eq(SysRoleMenu::getRoleId, id));
        return this.removeById(id);
    }


    /**
     * 获取用户角色信息
     *
     * @return 角色集合
     */
    @Override
    public Set<Integer> getRoleIds() {
        Authentication authentication = SecurityUtils.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Set<Integer> roleIds = new HashSet<>();
        authorities.stream().forEach(granted->{
            //System.out.println(granted.getAuthority());
            //根据角色编号查询角色ID
            SysRole one = baseMapper.selectOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, granted.getAuthority()));
            if(one!=null){
                roleIds.add(one.getRoleId());
            }

        });
        return roleIds;
    }

    /**
     * 判断角色是否存在
     * @return
     */
    @Override
    public R isRole(SysRole sysRole) {
        SysRole byId = this.getById(sysRole.getRoleId());
        if(byId!=null){
            if(!(sysRole.getRoleCode().equals(byId.getRoleCode()) || sysRole.getRoleName().equals(byId.getRoleName()))){
                List<SysRole> roleCodeList = this.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, sysRole.getRoleCode()));
                if(roleCodeList.size()>0){
                    return R.builder().code(CommonConstants.FAIL).message(String.format("角色编码【%s】已存在",sysRole.getRoleCode())).build();
                }
                List<SysRole> roleNameList = this.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleName, sysRole.getRoleName()));
                if(roleNameList.size()>0){
                    return R.builder().code(CommonConstants.FAIL).message(String.format("角色名称【%s】已存在",sysRole.getRoleName())).build();
                }
            }
        }
        return R.builder().code(CommonConstants.SUCCESS).build();
    }
}
