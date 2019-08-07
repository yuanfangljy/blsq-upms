package com.blsq.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blsq.admin.common.dto.OrgDTO;
import com.blsq.admin.common.vo.OrgVO;
import com.blsq.admin.common.vo.UserVO;
import com.blsq.admin.model.SysOrg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织 Mapper 接口
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */
public interface SysOrgMapper extends BaseMapper<SysOrg> {

    /**
     * 根据组织ID，查询相应组织详情
     * @param orgDTO
     * @return
     */
    IPage<List<OrgVO>> selectOrgWithUserByIdList(Page page, @Param("orgDTO") OrgDTO orgDTO);

    /**
     * 查询该组织是否是父类
     * @param orgId
     * @return
     */
    List<SysOrg> selectOrgByParentId(@Param("orgId") Integer orgId);

    /**
     * 根据组织Id,所有的子孙节点
     * @param orgId
     * @return
     */
    List<SysOrg> selectOrgDescendantList(@Param("orgId") Integer orgId);

    /**
     * 根据组织Id,当前组织以及下级的所有组织信息
     * @param page
     * @param orgDTO
     * @return
     */
    IPage selectOrgPage(Page page, @Param("orgDTO") OrgDTO orgDTO);

    /**
    * 根据组织Id,查询组织小区详情
     * @param orgId
     * @return
     */
    OrgVO selectOrgDetailsById(Integer orgId);
}
