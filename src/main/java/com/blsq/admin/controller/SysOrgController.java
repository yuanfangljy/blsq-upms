package com.blsq.admin.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONString;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.dto.MenuTree;
import com.blsq.admin.common.dto.OrgDTO;
import com.blsq.admin.common.dto.OrgTree;
import com.blsq.admin.common.util.CommonPageResponse;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.common.vo.*;
import com.blsq.admin.mapper.SysOrgHouseMapper;
import com.blsq.admin.mapper.SysOrgMapper;
import com.blsq.admin.model.SysOrg;
import com.blsq.admin.model.SysOrgHouse;
import com.blsq.admin.model.SysRole;
import com.blsq.admin.service.ISysOrgHouseService;
import com.blsq.admin.service.ISysOrgRelationService;
import com.blsq.admin.service.ISysOrgService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织 前端控制器
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/sysOrg")
@AllArgsConstructor
@Api(value = "org", description = "组织模块")
public class SysOrgController {

    private final ISysOrgService sysOrgService;
    private final ISysOrgHouseService sysOrgHouseService;
    private final SysOrgMapper sysOrgMapper;





    /**
     * 根据当前用户组织查询小区List
     * @param
     * @return
     */
    @PostMapping("/getHouseIdByOrgList")
    public R getOrgHouseList(){
        //1、获取当前用户的组织ID
        Integer[] orgIds = SecurityUtils.getUser().getOrgId();
        //2、查询组织的所有子孙后代
        List<String> houseIdListAll=new ArrayList<>();
        Arrays.stream(orgIds).forEach(orgId -> {
            List<SysOrg> orgDescendantList = sysOrgService.getOrgDescendantList(orgId);
            //3、得到所有的自己及子孙后代的组织ID
            List<Integer> orgIdList = orgDescendantList.stream().map(SysOrg::getOrgId).collect(Collectors.toList());
            //4、通过组织Id,查询所有的小区ID,进行去重
            List<String> houseIdList=new ArrayList<>();
            orgIdList.stream().forEach(oId->{
                Set<String> houseIds = sysOrgHouseService.list(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getOrgId, oId)).stream().map(SysOrgHouse::getHouseId).collect(Collectors.toSet());
                houseIdList.addAll(houseIds);
            });
            houseIdListAll.addAll(houseIdList);
        });

        return new R<>(houseIdListAll);
    }

    /**
     * 根据orgId,查询小区
     * @param
     * @return
     */
    @PostMapping("/getOrgHouseByOrgIdList")
    public R getOrgHouseByOrgIdList(@RequestBody SysOrgHouse orgHouse){
        List<SysOrgHouse> houses = sysOrgHouseService.list(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getOrgId, orgHouse.getOrgId()));
        return new R<>(houses);
    }

    /**
     * 根据组织Id,查询组织小区详情
     * @param sysOrg
     * @return
     */
    @PostMapping(value = "/getOrgDetailsById")
    public R getOrgDetailsById(@RequestBody SysOrg sysOrg) throws Exception {
        if(sysOrg.getOrgId()==null){
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("组织ID不能为空")
                    .build();
        }
        SysOrg byId = sysOrgService.getById(sysOrg.getOrgId());
        if((byId == null) || (byId.getDelFlag().equals(CommonConstants.FAIL))){
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("查询信息不存在")
                    .build();
        }
        OrgVO orgVO=sysOrgService.getOrgDetailsById(sysOrg.getOrgId());

        return new R<>(sysOrgService.getHouseVOURL(orgVO));
    }

    /**
     * 根据组织Id,当前组织以及下级的所有组织信息
     *
     * @return
     */
    @PostMapping(value = "/getOrgListById")
    public R getOrgListById(Page page,OrgDTO orgDTO){
        if(orgDTO.getOrgId()==null){
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("组织ID不能为空")
                    .build();
        }
        IPage orgPage = sysOrgService.getOrgPage(page,orgDTO);
        CommonPageResponse commonPageResponse = CommonPageResponse.makeSuccessResp(orgPage);
        return commonPageResponse;
    }

    /**
     * 查询当前用户的组织树
     * TODO:
     * @return
     */
    @PostMapping(value = "/getOrgTree")
    public R getOrgTree(){
        //1、获取符合条件的组织
        Integer[] orgIds = SecurityUtils.getUser().getOrgId();
        //2、根据组织Id，在sys_org_relation中的所有子孙
        List<OrgTree> orgTree = new ArrayList<>();
        Arrays.stream(orgIds).forEach(orgId->{
            //3、根据组织所有子孙Id，查询出所有的组织信息
            List<SysOrg> orgList=sysOrgService.getOrgDescendantList(orgId);
            //4、取orgList的parentId，对小的数
            List<Integer> parentId = orgList.stream().map(SysOrg::getParentId).collect(Collectors.toList());
            IntSummaryStatistics stats = parentId.stream().mapToInt((x) -> x).summaryStatistics();
            int parentIdMin = stats.getMin();
            //5、对所有组织，进行树状构建
            List<OrgTree> orgTreeById = sysOrgService.getOrgTree(orgList,parentIdMin);
            //6、添加到集合中
            orgTree.addAll(orgTreeById);
        });
        return new R<>(orgTree);
    }

    /**
     * 添加组织
     *
     * @param orgDTO 组织信息
     * @return success、false
     */
    @PostMapping("/save")
    public R save(@Valid @RequestBody OrgDTO orgDTO) {
        List<SysOrg> list = sysOrgService.list(new QueryWrapper<SysOrg>().lambda().eq(SysOrg::getOrgName, orgDTO.getOrgName()));
        if(list.size()>0){
            return R.builder().code(CommonConstants.FAIL).message("组织名称已存在，重新输入").build();
        }
        return new R<>(sysOrgService.saveOrgAndHouse(orgDTO));
    }

    /**
     * 修改组织
     *
     * @param orgDTO 组织信息
     * @return success/false
     */
    @PostMapping("/update")
    public R update(@Valid @RequestBody OrgDTO orgDTO) {
        SysOrg byId = sysOrgService.getById(orgDTO.getOrgId());
        if(byId==null){
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("操作信息不存在")
                    .build();
        }
        if(!byId.getOrgName().equals(orgDTO.getOrgName())){
            List<SysOrg> list = sysOrgService.list(new QueryWrapper<SysOrg>().lambda().eq(SysOrg::getOrgName, orgDTO.getOrgName()));
            if(list.size()>0){
                return R.builder().code(CommonConstants.FAIL).message("组织名称已存在，重新输入").build();
            }
        }
        return new R<>(sysOrgService.updateOrgAndHouse(orgDTO,SecurityUtils.getUser().getUsername()));
    }

    /**
     * 删除组织
     *
     * @param sysOrg
     * @return
     */
    @PostMapping("/removeById")
    public R removeById(@RequestBody SysOrg sysOrg) {
        List<SysOrg> orgList = sysOrgMapper.selectOrgByParentId(sysOrg.getOrgId());
        if (CollUtil.isNotEmpty(orgList)) {
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("组织含有下级不能删除").build();
        }
        return new R<>(sysOrgService.removeOrgById(sysOrg.getOrgId()));
    }


//===================================================================================================

    /**
     * 查询当前用户的所有组织
     * @return
     */
    @PostMapping(value = "/getOrgUserList")
    public R getOrgUserList(){
        //1、先查询出，当前用户的组织Id
        Integer[] orgIds = SecurityUtils.getUser().getOrgId();

        Set<MenuVO> all = new HashSet<>();
        //2、通过orgId查询出，它的父Id
        Set<Integer> parentIds=new HashSet<>();
        Arrays.stream(orgIds).forEach(orgId->{
            SysOrg org = sysOrgService.getById(orgId);
            if(org!=null){
                parentIds.add(org.getParentId());
            }
        });
        //3、通过组织Id,查询所有子集组织
        Set<OrgTree> orgTrees = new HashSet<>();
        Arrays.stream(orgIds).forEach(orgId->{
            List<OrgTree> orgTreeById = sysOrgService.getOrgTreeById(orgId);
            orgTrees.addAll(orgTreeById);
        });
        return new R<>(orgTrees);
    }



    /**
     * 根据当前用户组织Id,树形组织集合
     *
     * @return 用户树形组织
     */
    @PostMapping(value = "/tree-user")
    public R getTreeUser(){
        // 获取符合条件的组织
        Integer userId = SecurityUtils.getUser().getId();
        List<SysOrg> orgList = sysOrgService.list(new QueryWrapper<SysOrg>().lambda().eq(SysOrg::getUserId,userId));
        Set<SysOrg> all = new HashSet<>();
        all.addAll(orgList);

        List<OrgTree> treeList = all.stream()
                .filter(org -> !CommonConstants.FAIL.equals(org.getDelFlag()))
                .map(OrgTree::new)
                .sorted(Comparator.comparingInt(OrgTree::getSort))
                .collect(Collectors.toList());

        return new R<>(TreeUtil.build(treeList, -1));
    }


    /**????????????????????????????????????????
     * 根据组织ID，查询相应组织详情
     *
     * @return 树形组织
     */
    @PostMapping(value = "/getOrgByIdPage")
    public R getOrgByIdPage(Page page, OrgDTO orgDTO){
        if(orgDTO.getOrgId()==null){
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .message("组织ID不能为空")
                    .build();
        }
        IPage orgVOList = sysOrgService.getOrgWithUserByIdList(page,orgDTO);
        return new R<>(orgVOList);
    }

}

