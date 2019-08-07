package com.blsq.admin.service;

import com.blsq.admin.common.util.R;
import com.blsq.admin.common.vo.HouseVO;
import com.blsq.admin.model.SysOrgHouse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */
public interface ISysOrgHouseService extends IService<SysOrgHouse> {

    /**
     * 保存组织区域
     * @return
     */
    Boolean saveOrgHouse(Integer orgId,List<String> houseId);
    /**
     * 更新组织区域
     * @return
     */
    Boolean updateOrgHouse(Integer orgId,List<String> houseId);

    /**
     * 获取到所有的小区列表
     * @return
     */
    List<HouseVO> getHouseVOList();


    /**
     * 根据orgId，获取上级父类，中的orgId；如果没有上级父类，就获取本身的
     * @param orgId
     * @return  待选的小区
     */
    List<HouseVO> waitingSelectHouseList(Integer orgId,List<HouseVO> houseVOList);


    /**
     * 默认超级管理员，所以小区
     * @param houseVOList
     * @return
     */
    R<Boolean> allocationSuperAdminAllHouse(List<HouseVO> houseVOList);

}
