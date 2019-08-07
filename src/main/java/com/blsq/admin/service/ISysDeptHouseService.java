package com.blsq.admin.service;

import com.blsq.admin.model.SysDeptHouse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */

public interface ISysDeptHouseService extends IService<SysDeptHouse> {
    /**
     * 更新部门区域
     * @return
     */
    Boolean saveDeptHouse(Integer deptId,String houseId);
}
