package com.blsq.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 接口名单
 * </p>
 *
 * @author liujiayi
 * @since 2019-06-03
 */
@Data
public class SysInterfaceList extends Model<SysInterfaceList> {

    private static final long serialVersionUID = 1L;

    /**
     * 接口ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建用户ID
     */
    private Integer createUserId;
    /**
     * 名称
     */
    @NotNull(message = "接口名称不能为空")
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 0：白名单 1：黑名单
     */
    @NotNull(message = "接口类型不能为空")
    private String type;

    /**
     * 接口url
     */
    @NotNull(message = "接口地址不能为空")
    private String url;

    /**
     * 是否删除  1：已删除  0：正常
     */
    private String delFlag;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private Integer version;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
