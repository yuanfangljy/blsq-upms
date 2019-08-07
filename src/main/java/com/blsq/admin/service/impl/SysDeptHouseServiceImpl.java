package com.blsq.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.model.SysDeptHouse;
import com.blsq.admin.mapper.SysDeptHouseMapper;
import com.blsq.admin.model.SysOrgHouse;
import com.blsq.admin.service.ISysDeptHouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */
@Service
@AllArgsConstructor
public class SysDeptHouseServiceImpl extends ServiceImpl<SysDeptHouseMapper, SysDeptHouse> implements ISysDeptHouseService {

    /**
     * 更新部门区域
     * @param deptId
     * @param houseIds
     * @return
     */
    @Override
    public Boolean saveDeptHouse(Integer deptId, String houseIds) {
        //1、先删除所有存在的orgId对应数据
        this.remove(new QueryWrapper<SysDeptHouse>().lambda().eq(SysDeptHouse::getDeptId,deptId));
        //2、再添加批量保存
        if (StrUtil.isBlank(houseIds)) {
            return Boolean.TRUE;
        }
        List<SysDeptHouse> deptHouseList = Arrays
                .stream(houseIds.split(","))
                .map(houseId->{
                    SysDeptHouse deptHouse=new SysDeptHouse();
                    deptHouse.setHouseId(houseId);
                    deptHouse.setDeptId(deptId);
                    return deptHouse;
                }).collect(Collectors.toList());
        return this.saveBatch(deptHouseList);
    }
}
