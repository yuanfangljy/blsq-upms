package com.blsq.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@Data
public class SysDept extends Model<SysDept> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "dept_id", type = IdType.AUTO)
    private Integer deptId;

    /**
     * 组织Id
     */
    @NotNull(message = "组织Id不能为空")
    private Integer orgId;

    /**
     * 创建人Id
     */
    private Integer userId;

    /**
     * 部门名称
     */
    @NotNull(message = "部门名称不能为空")
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除  1：已删除  0：正常
     */
    @TableLogic
    private String delFlag;

    private Integer parentId;

    private Integer tenantId;



    @Override
    protected Serializable pkVal() {
        return this.deptId;
    }


}
