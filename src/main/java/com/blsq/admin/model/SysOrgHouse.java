package com.blsq.admin.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */
@Data
public class SysOrgHouse extends Model<SysOrgHouse> {

    private static final long serialVersionUID = 1L;

    /**
     * 组织Id
     */
    private Integer orgId;

    /**
     * 小区Id
     */
    private String houseId;



    @Override
    protected Serializable pkVal() {
        return this.orgId;
    }

}
