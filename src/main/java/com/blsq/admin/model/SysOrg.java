package com.blsq.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 组织
 * </p>
 *
 * @author liujiayi
 * @since 2019-05-29
 */
@Data
public class SysOrg extends Model<SysOrg> {

    private static final long serialVersionUID = 1L;

    /**
     * 组织Id
     */
    @TableId(value = "org_id", type = IdType.AUTO)
    private Integer orgId;

    /**
     * 创建人Id
     */
    private Integer userId;

    /**
     * 组织名称
     */
    @NotNull(message = "组织名称不能为空")
    private String orgName;

    /**
     * 组织编码
     */
    @NotNull(message = "组织编码不能为空")
    private String orgCode;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 父类Id
     */
    private Integer parentId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标识（0-正常,1-删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 所属租户
     */
    private Integer tenantId;

    /**
     * 版本号
     */
    private Integer version;




    @Override
    protected Serializable pkVal() {
        return this.orgId;
    }


}
