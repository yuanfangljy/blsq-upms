package com.blsq.admin.service.impl;



import javax.validation.Valid;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.dto.OrgDTO;
import com.blsq.admin.common.dto.OrgTree;
import com.blsq.admin.common.util.JSONUtils;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.common.vo.HouseVO;
import com.blsq.admin.common.vo.OrgVO;
import com.blsq.admin.common.vo.TreeUtil;
import com.blsq.admin.mapper.SysOrgMapper;
import com.blsq.admin.mapper.SysUserMapper;
import com.blsq.admin.model.*;
import com.blsq.admin.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 组织 服务实现类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */
@Service
@Slf4j
@AllArgsConstructor
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {

    private final SysOrgMapper sysOrgMapper;
    private final ISysOrgHouseService sysOrgHouseService;
    private final ISysOrgRoleService sysOrgRoleService;
    private final ISysOrgRelationService sysOrgRelationService;
    private final ISysUserOrgService sysUserOrgService;
    private final SysUserMapper sysUserMapper;


    /**
     * 根据组织ID，查询相应组织详情
     * @param orgDTO
     * @return
     */
    @Override
    public IPage getOrgWithUserByIdList(Page page, OrgDTO orgDTO){
        return sysOrgMapper.selectOrgWithUserByIdList(page,orgDTO);
    }

    /**
     * 获取所有的组织本级及下级的tree
     * @return
     */
    @Override
    public List<OrgTree> getOrgTreeById(Integer orgId) {
        List<SysOrg> orgList = sysOrgMapper.selectList(new QueryWrapper<SysOrg>().lambda().ne(SysOrg::getDelFlag, CommonConstants.FAIL));
        return getOrgTree(orgList,orgId);
    }

    /**
     * 构建组织树
     *
     * @param orgs 组织
     * @return
     */
    @Override
    public List<OrgTree> getOrgTree(List<SysOrg> orgs,Integer orgId) {
        List<OrgTree> treeList = orgs.stream()
               // .filter(org -> CommonConstants.FAIL.equals(org.getDelFlag()))
                .map(org -> {
                    OrgTree node = new OrgTree();
                    node.setId(org.getOrgId());
                    node.setParentId(org.getParentId());
                    node.setOrgName(org.getOrgName());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, orgId);
    }

    /**
     * 根据组织Id,当前组织以及下级的所有组织信息
     * @param page
     * @param orgDTO
     * @return
     */
    @Override
    public IPage getOrgPage(Page page, OrgDTO orgDTO) {
        return baseMapper.selectOrgPage(page,orgDTO);
    }

    /**
     * 根据当前用户组织查询小区List
     * @return
     */
    @Override
    public Set<String> getHouseIdsByOrgList(String username) {
        //1、根据用户名，获取当前用户的组织ID
        SysUser user = sysUserMapper.getUserByUsername(username);
        Set<Integer> orgIds = sysUserOrgService.list(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getUserId, user.getUserId())).stream().map(SysUserOrg::getOrgId).collect(Collectors.toSet());
        //2、查询组织的所有子孙后代
        Set<String> houseIdListAll=new HashSet<>();
        orgIds.forEach(orgId -> {
            List<SysOrg> orgDescendantList = sysOrgMapper.selectOrgDescendantList(orgId);
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
        return houseIdListAll;
    }

    /**
     * 根据组织Id,查询组织小区详情
     * @param orgId
     * @return
     */
    @Override
    public OrgVO getOrgDetailsById(Integer orgId) {
        return sysOrgMapper.selectOrgDetailsById(orgId);
    }

    /**
     * 获取所有小区
     * @return
     */
    @Override
    public OrgVO getHouseIdURL(OrgVO orgVO) {
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
            List<HouseVO> houseVOList=new ArrayList<>();
            houseVoList.forEach(houseVo->{
                HouseVO houseVO = JSONUtils.strToObject(houseVo.toString(), HouseVO.class);
                houseVOList.add(houseVO);
            });
            log.debug("所有小区--------"+houseVOList.toString());
            log.debug("当前用户组织小区Id--------"+orgVO.getSelectedHouseList().toString());
            if(orgVO.getSelectedHouseList()!=null){
                //1、保存所有待选小区
                List<HouseVO> waitingSelectHouseList = houseVOList.stream()
                        .filter(item -> !orgVO.getSelectedHouseList().stream()
                                .map(e -> e.getId())
                                .collect(Collectors.toList())
                                .contains(item.getId()))
                        .collect(Collectors.toList());
                //2、保存所有已选小区
                List<HouseVO> selectedHouseList = houseVOList.stream()
                        .filter(item -> orgVO.getSelectedHouseList().stream()
                                .map(e -> e.getId())
                                .collect(Collectors.toList())
                                .contains(item.getId()))
                        .collect(Collectors.toList());
                waitingSelectHouseList.forEach(houseVO -> {
                    log.debug("---待选 intersection---"+houseVO.toString());
                });
                selectedHouseList.forEach(houseVO -> {
                    log.debug("---已选 intersection---"+houseVO.toString());
                });
                //3、保存到Map中
                orgVO.setWaitingSelectHouseList(waitingSelectHouseList);
                orgVO.setSelectedHouseList(selectedHouseList);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error("小区查询，请求失败，请稍后再试");
        }
       return orgVO;
    }

    /**
     * 根据组织Id,查询所有待选和已选的小区
     * @param orgVO
     * @return
     * @throws Exception
     */
    @Override
    public OrgVO getHouseVOURL(OrgVO orgVO) throws Exception {
        //1、得到所有的自己及子孙后代的组织ID
        List<SysOrg> orgDescendantList = sysOrgMapper.selectOrgDescendantList(orgVO.getOrgId());
        List<Integer> orgIdList = orgDescendantList.stream().map(SysOrg::getOrgId).collect(Collectors.toList());
        //2、根据子类及自己的orgId，到sys_org_house查询所有的小区，进行去重
        Set<HouseVO> houseVOSet=new HashSet<>();
        orgIdList.forEach(orgId->{
            Set<HouseVO> houseIds = sysOrgHouseService.list(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getOrgId, orgId)).stream().map(houseId -> {
                HouseVO houseVO = new HouseVO();
                houseVO.setId(houseId.getHouseId());
                return houseVO;
            }).collect(Collectors.toSet());
            houseVOSet.addAll(houseIds);
        });
        //3、获取到所有小区列表
        List<HouseVO> houseVOList = sysOrgHouseService.getHouseVOList();
        //4、保存所有已选小区
        List<HouseVO> selectedHouseList = houseVOList.stream()
                .filter(item -> houseVOSet.stream()
                        .map(e -> e.getId())
                        .collect(Collectors.toList())
                        .contains(item.getId()))
                .collect(Collectors.toList());
        //5、获取到自己orgId的上级父类，中的orgId；如果没有上级父类，就获取本身的;待选小区
        List<HouseVO> waitingSelectHouseList = sysOrgHouseService.waitingSelectHouseList(orgVO.getOrgId(), houseVOList);
        orgVO.setSelectedHouseList(selectedHouseList);
        //6、判断已选的小区是否，和待选的小区有重合；如果有把待选的剔除
        List<HouseVO> filtrationWaitingSelectHouseList = waitingSelectHouseList.stream()
                .filter(item -> !selectedHouseList.stream()
                        .map(e -> e.getId())
                        .collect(Collectors.toList())
                        .contains(item.getId()))
                .collect(Collectors.toList());

        orgVO.setWaitingSelectHouseList(filtrationWaitingSelectHouseList);
        return orgVO;
    }



    /**
     * 添加组织
     * @param orgDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrgAndHouse(OrgDTO orgDTO) {
        SysOrg sysOrg=new SysOrg();
        BeanUtils.copyProperties(orgDTO, sysOrg);
        sysOrg.setUserId(SecurityUtils.getUser().getId());
        //添加组织基本信息
        this.save(sysOrg);
        //添加组织角色信息
        sysOrgRoleService.saveUpdateOrgRole(sysOrg.getOrgId(),orgDTO.getRoleIds());
        //添加组织关系表
        sysOrgRelationService.insertOrgRelation(sysOrg);
        //添加组织小区信息
        sysOrgHouseService.saveOrgHouse(sysOrg.getOrgId(),orgDTO.getHouseIds());
        return Boolean.TRUE;
    }

    /**
     * 修改组织
     * @param orgDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user_details", key = "#username")
    public Boolean updateOrgAndHouse(@Valid OrgDTO orgDTO, String username) {
        SysOrg sysOrg=new SysOrg();
        BeanUtils.copyProperties(orgDTO, sysOrg);
        //组织基本信息
        this.updateById(sysOrg);
        //更新组织关系表
        SysOrgRelation relation=new SysOrgRelation();
        relation.setAncestor(sysOrg.getParentId());
        relation.setDescendant(sysOrg.getOrgId());
        //sysOrgRelationService.updateOrgRelation(relation);
        //更新组织角色信息
        sysOrgRoleService.saveUpdateOrgRole(sysOrg.getOrgId(),orgDTO.getRoleIds());
        //更新组织小区信息
        sysOrgHouseService.updateOrgHouse(sysOrg.getOrgId(),orgDTO.getHouseIds());

        return Boolean.TRUE;
    }

    /**
     * 删除组织
     * @param orgId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeOrgById(Integer orgId) {
        //删除，组织基本信息
        this.removeById(orgId);
        //删除，组织小区
        sysOrgHouseService.remove(new QueryWrapper<SysOrgHouse>().lambda().eq(SysOrgHouse::getOrgId,orgId));
        //删除，组织角色
        sysOrgRoleService.remove(new QueryWrapper<SysOrgRole>().lambda().eq(SysOrgRole::getOrgId,orgId));
        //删除组织
        //级联删除部门
       /* List<Integer> idList = sysOrgRelationService
                .list(new QueryWrapper<SysOrgRelation>().lambda()
                        .eq(SysOrgRelation::getAncestor, orgId))
                .stream()
                .map(SysOrgRelation::getDescendant)
                .collect(Collectors.toList());

        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }*/

        //删除部门级联关系
        sysOrgRelationService.deleteAllOrgRealtion(orgId);
        return Boolean.TRUE;
    }

    /**
     * 根据组织Id,所有的子孙节点
     * @param orgId
     * @return
     */
    @Override
    public List<SysOrg> getOrgDescendantList(Integer orgId) {
        return sysOrgMapper.selectOrgDescendantList(orgId);
    }
}
