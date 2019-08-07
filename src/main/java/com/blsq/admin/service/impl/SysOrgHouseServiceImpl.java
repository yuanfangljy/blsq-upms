package com.blsq.admin.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.util.JSONUtils;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.common.vo.HouseVO;
import com.blsq.admin.common.vo.OrgVO;
import com.blsq.admin.mapper.SysOrgMapper;
import com.blsq.admin.model.*;
import com.blsq.admin.mapper.SysOrgHouseMapper;
import com.blsq.admin.service.ISysOrgHouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blsq.admin.service.ISysOrgRelationService;
import com.blsq.admin.service.ISysOrgRoleService;
import com.blsq.admin.service.ISysRoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
@Slf4j
public class SysOrgHouseServiceImpl extends ServiceImpl<SysOrgHouseMapper, SysOrgHouse> implements ISysOrgHouseService {

    private final ISysOrgRelationService sysOrgRelationService;
    private final SysOrgMapper sysOrgMapper;
    private final ISysOrgRoleService sysOrgRoleService;
    private final ISysRoleService sysRoleService;

    /**
     * 保存组织区域
     * @param orgId
     * @param houseIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("all")
    public Boolean saveOrgHouse(Integer orgId, List<String> houseIds) {
        //1、查询orgId，下面的所有子节点
        List<Integer> orgIdList=sysOrgMapper.selectOrgDescendantList(orgId).stream().map(SysOrg::getOrgId).collect(Collectors.toList());
        //2、先删除所有存在的orgId对应数据
        orgIdList.forEach(oId->{
            this.remove(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getOrgId,oId));
        });
        //2、再添加批量保存
        if (houseIds.size()==0) {
            return Boolean.TRUE;
        }
        String houses = houseIds.stream().collect(Collectors.joining(","));
        //4、根据组织Id，查询所有的父节点及自己
        Set<Integer> orgParentId = sysOrgRelationService.list(new QueryWrapper<SysOrgRelation>().lambda().eq(SysOrgRelation::getDescendant, orgId)).stream().map(SysOrgRelation::getAncestor).collect(Collectors.toSet());
        //3、将每一个父节点，都加上这个小区，都更新
        List<SysOrgHouse> orgHouseLists=new ArrayList<>();
        orgParentId.forEach(orgPId->{
            List<SysOrgHouse> orgHouseList = Arrays
                    .stream(houses.split(","))
                    .map(houseId->{
                        //判断父级是否存在了，此信息
                        List<SysOrgHouse> list = this.list(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getHouseId, houseId).eq(SysOrgHouse::getOrgId, orgPId));
                        this.remove(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getHouseId, houseId).eq(SysOrgHouse::getOrgId, orgPId));
                        SysOrgHouse orgHouse=new SysOrgHouse();
                            orgHouse.setHouseId(houseId);
                            orgHouse.setOrgId(orgPId);
                        return orgHouse;
                    }).collect(Collectors.toList());
            orgHouseLists.addAll(orgHouseList);
        });
        return this.saveBatch(orgHouseLists);
    }

    /**
     * 更新组织小区
     * @param orgId
     * @param houseIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("all")
    public Boolean updateOrgHouse(Integer orgId, List<String> houseIds) {

        //1、根据orgId，在sys_org_house中查询所有小区
        List<String> houseIdLists = this.list(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getOrgId, orgId)).stream().map(SysOrgHouse::getHouseId).collect(Collectors.toList());
        //2、与现在传来的小区进行对比，筛选差集
        List<String> deleteHouseIdList = new ArrayList<>();
        if(houseIdLists.size()>houseIds.size()){
            deleteHouseIdList = houseIdLists.stream().filter(item -> !houseIds.contains(item))
                    .collect(Collectors.toList());
        }else if(houseIdLists.size()<houseIds.size()){
            deleteHouseIdList = houseIds.stream().filter(item -> !houseIdLists.contains(item))
                    .collect(Collectors.toList());
        }else{
            List<String> collect1 = houseIdLists.stream().filter(item -> !houseIds.contains(item))
                    .collect(Collectors.toList());
            deleteHouseIdList.addAll(collect1);
            List<String> collect2 = houseIds.stream().filter(item -> !houseIdLists.contains(item))
                    .collect(Collectors.toList());
            deleteHouseIdList.addAll(collect2);
        }

        //3、查询orgId，下面的所有子节点
        List<Integer> orgIdList=sysOrgMapper.selectOrgDescendantList(orgId).stream().map(SysOrg::getOrgId).collect(Collectors.toList());
        //4、先删除orgId所有下级及自己，存在的houseId对应数据
        List<String> finalDeleteHouseIdList = deleteHouseIdList;
        orgIdList.forEach(oId->{
            finalDeleteHouseIdList.forEach(deleteHouseId->{
                this.remove(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getOrgId,oId).eq(SysOrgHouse::getHouseId,deleteHouseId));
            });
        });
        //5、再添加批量保存
        if (houseIds.size()==0) {
            return Boolean.TRUE;
        }
        String houses = houseIds.stream().collect(Collectors.joining(","));
        //6、根据组织Id，查询所有的父节点及自己
        Set<Integer> orgParentId = sysOrgRelationService.list(new QueryWrapper<SysOrgRelation>().lambda().eq(SysOrgRelation::getDescendant, orgId)).stream().map(SysOrgRelation::getAncestor).collect(Collectors.toSet());
        if(orgParentId.size()==1){
            houseIds.forEach(houseId->{
                //判断父级是否存在了，此信息
                List<SysOrgHouse> list = this.list(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getHouseId, houseId).eq(SysOrgHouse::getOrgId, orgId));
                this.remove(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getHouseId, houseId).eq(SysOrgHouse::getOrgId, orgId));
                SysOrgHouse sysOrgHouse=new SysOrgHouse();
                sysOrgHouse.setOrgId(orgId);
                sysOrgHouse.setHouseId(houseId);
                this.save(sysOrgHouse);
            });
            return Boolean.TRUE;
        }
        System.out.println(orgParentId.toString());
        //3、将每一个父节点，都加上这个小区，都更新
        List<SysOrgHouse> orgHouseLists=new ArrayList<>();
        orgParentId.forEach(orgPId->{
            List<SysOrgHouse> orgHouseList = Arrays
                    .stream(houses.split(","))
                    .map(houseId->{
                        //判断父级是否存在了，此信息
                        List<SysOrgHouse> list = this.list(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getHouseId, houseId).eq(SysOrgHouse::getOrgId, orgPId));
                        this.remove(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getHouseId, houseId).eq(SysOrgHouse::getOrgId, orgPId));
                        SysOrgHouse orgHouse=new SysOrgHouse();
                        orgHouse.setHouseId(houseId);
                        orgHouse.setOrgId(orgPId);
                        return orgHouse;
                    }).collect(Collectors.toList());
            orgHouseLists.addAll(orgHouseList);
        });
        return this.saveBatch(orgHouseLists);
    }

    /**
     * 获取到所有的小区列表
     * @return
     */
    @Override
    public List<HouseVO> getHouseVOList() {

        List<HouseVO> houseVOList=new ArrayList<>();
        try {
            //创建URL
            URL url=new URL(CommonConstants.HOUSE_RUL);
            //返回结果集
            StringBuffer document =new StringBuffer();
            //创建链接
            URLConnection conn=url.openConnection();
            //返回结果集
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                document.append(line);
            }
            reader.close();
            JSONObject jsonObject=new JSONObject(document.toString());
            Object data = jsonObject.get("data");
            JSONArray houseVoList= JSONUtil.parseArray(data.toString());
            houseVoList.forEach(houseVo->{
                HouseVO houseVO = JSONUtils.strToObject(houseVo.toString(), HouseVO.class);
                houseVOList.add(houseVO);
            });
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询小区列表，请求失败，请稍后再试");
        }
        this.allocationSuperAdminAllHouse(houseVOList);
        return houseVOList;
    }

    /**
     * 根据orgId，获取上级父类，中的orgId；如果没有上级父类，就获取本身的
     * @param orgId
     * @return  待选的小区
     */
    @Override
    public List<HouseVO> waitingSelectHouseList(Integer orgId,List<HouseVO> houseVOList) {
        //1、获取到所有上级orgId
        Set<Integer> orgParentId = sysOrgRelationService.list(new QueryWrapper<SysOrgRelation>().lambda().eq(SysOrgRelation::getDescendant, orgId)).stream().map(SysOrgRelation::getAncestor).collect(Collectors.toSet());
        //2、获取到除自己外，最大的orgId
        int maxOrgId=0;
        if(orgParentId.size()==1){
            maxOrgId=orgId;
        }else{
            List<Integer> collect = orgParentId.stream().filter((opId) ->!opId.equals(orgId.intValue())).collect(Collectors.toList());
            maxOrgId = collect.stream().mapToInt((opId) -> opId).summaryStatistics().getMax();

        }

        //???????????????????????????????????
        //3、根据maxOrgId，获取到所有待选的小区?还是获取到，已经选择了的小区
        OrgVO orgVO=sysOrgMapper.selectOrgDetailsById(maxOrgId);
        //4、保存所有待选小区
        List<HouseVO> waitingSelectHouseList=null;
        if(orgParentId.size()==1){
             waitingSelectHouseList = houseVOList.stream()
                    .filter(item -> !orgVO.getSelectedHouseList().stream()
                            .map(e -> e.getId())
                            .collect(Collectors.toList())
                            .contains(item.getId()))
                    .collect(Collectors.toList());
        }else{
             waitingSelectHouseList = houseVOList.stream()
                    .filter(item -> orgVO.getSelectedHouseList().stream()
                            .map(e -> e.getId())
                            .collect(Collectors.toList())
                            .contains(item.getId()))
                    .collect(Collectors.toList());
        }
        return waitingSelectHouseList;
    }

    /**
     * 默认超级管理员，所以小区
     * @param houseVOList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> allocationSuperAdminAllHouse(List<HouseVO> houseVOList) {
        //1、获取到当前用户的，所以组织Id
        Integer[] orgIds = SecurityUtils.getUser().getOrgId();
        Arrays.stream(orgIds).forEach(orgId->{
            //2、根据组织Id，查询角色是否是超级管理员ROLE_SUPER
            SysOrgRole byId = sysOrgRoleService.getOne(new QueryWrapper<SysOrgRole>().lambda().eq(SysOrgRole::getOrgId,orgId));
            SysRole sysRole = sysRoleService.getById(byId.getRoleId());
            if(sysRole.getRoleCode().equals(CommonConstants.SUPER_ADMIN)){
                this.remove(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getOrgId,orgId));
                SysOrgHouse sysOrgHouse=new SysOrgHouse();
                sysOrgHouse.setOrgId(orgId);
                houseVOList.forEach(houseVO->{
                    sysOrgHouse.setHouseId(houseVO.getId());
                    this.save(sysOrgHouse);
                });
            }
        });
        return new R(Boolean.TRUE);
    }


}
