package com.blsq.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blsq.admin.common.dto.OrgDTO;
import com.blsq.admin.common.dto.OrgTree;
import com.blsq.admin.common.vo.OrgVO;
import com.blsq.admin.model.SysOrg;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 组织 服务类
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */
public interface ISysOrgService extends IService<SysOrg> {


    /**
     * 根据组织ID，查询相应组织详情
     * @param orgDTO
     * @return
     */
    IPage getOrgWithUserByIdList(Page page, OrgDTO orgDTO);

    /**
     * 获取所有的组织本级及下级的tree
     * @return
     */
    List<OrgTree> getOrgTreeById(Integer orgId);

    /**
     * 添加组织
     * @param orgDTO
     * @return
     */
    Boolean saveOrgAndHouse(OrgDTO orgDTO);

    /**
     * 修改组织
     * @param orgDTO
     * @return
     */
    Boolean updateOrgAndHouse(@Valid OrgDTO orgDTO,String username);


    /**
     * 删除组织，以及对应的区域,和关系表
     * @param orgId
     * @return
     */
    Boolean removeOrgById(Integer orgId);

    /**
     * 根据组织Id,所有的子孙节点
     * @param orgId
     * @return
     */
    List<SysOrg> getOrgDescendantList(Integer orgId);

    /**
     * 构建组织树
     * @param orgs
     * @param orgId
     * @return
     */
    List<OrgTree> getOrgTree(List<SysOrg> orgs,Integer orgId);

    /**
     * 根据组织Id,当前组织以及下级的所有组织信息
     *
     * @param page
     * @param orgDTO
     * @return
     */
    IPage getOrgPage(Page page, OrgDTO orgDTO);

    /**
     * 根据当前用户组织查询小区List
     * @return
     */
    Set<String> getHouseIdsByOrgList(String username);

    /**
     * 根据组织Id,查询组织小区详情
     * @param orgId
     * @return
     */
    OrgVO getOrgDetailsById(Integer orgId);

    /**
     * 获取所有小区
     * @return
     */
    OrgVO getHouseIdURL(OrgVO orgVO) throws Exception;

    /**
     * 根据组织Id,查询所有待选和已选的小区
     * @param orgVO
     * @return
     * @throws Exception
     */
    OrgVO getHouseVOURL(OrgVO orgVO) throws Exception;
}
