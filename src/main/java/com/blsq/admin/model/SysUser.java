package com.blsq.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@Data
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "姓名不能为空")
    private String realname;

    @NotNull(message = "用户密码不能为空")
    private String password;

    /**
     * 随机盐
     */
    private String salt;

    /**
     * 简介
     */
    @NotNull(message = "电话号码不能为空")
    private String phone;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 邮箱
     */
    @NotNull(message = "邮箱不能为空")
    private String mailbox;
    /**
     * 身份证
     */
    private String identityCard;

    /**
     * 部门ID
     */
    private Integer deptId;


    /**
     * 是否删除  1：已删除  0：正常
     */
    @TableLogic
    private String delFlag;

    /**
     * 0-正常，9-锁定
     */
    private String lockFlag;

    private Integer tenantId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 版本号
     */
    private Integer version;

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }


}
