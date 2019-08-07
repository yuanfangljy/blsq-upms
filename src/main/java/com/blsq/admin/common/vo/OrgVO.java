/**
 * Copyright (c) 2019-2030, 云宝网络科技有限公司 All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the 云宝网络科技有限公司 developer nor the names of its
 * 注意：本内容仅限于云宝网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 * Author: liujiayi
 */
package com.blsq.admin.common.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.blsq.admin.model.SysOrgHouse;
import com.blsq.admin.model.SysRole;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 类描述：
 * </p>
 *
 * @author liujiayi
 * @since 2019/5/29 14:11
 * @version：1.0
 */
@Data
public class OrgVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 组织Id
     */
    private Integer orgId;

    /**
     * 创建人Id
     */
    private Integer userId;

    private String username;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织编码
     */
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
    private String delFlag;

    /**
     * 所属租户
     */
    private Integer tenantId;

    /**
     * 版本号
     */
    private Integer version;

    private List<HouseVO> selectedHouseList;
    private List<SysRole> roleList;
    private List<HouseVO> waitingSelectHouseList;
}
