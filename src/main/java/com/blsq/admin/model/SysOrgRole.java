package com.blsq.admin.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-30
 */
public class SysOrgRole extends Model<SysOrgRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 组织ID
     */
    private Integer orgId;

    /**
     * 角色ID
     */
    private Integer roleId;


    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    protected Serializable pkVal() {
        return this.orgId;
    }

    @Override
    public String toString() {
        return "SysOrgRole{" +
        "orgId=" + orgId +
        ", roleId=" + roleId +
        "}";
    }
}
