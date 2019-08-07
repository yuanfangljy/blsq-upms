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

import com.blsq.admin.model.SysOrg;
import com.blsq.admin.model.SysRole;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 类描述：
 * </p>
 *
 * @author liujiayi
 * @since 2019/5/31 17:45
 * @version：1.0
 */
@Data
public class UserOrgRoleVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String username;
    private String salt;
    private String realname;
    private String mailbox;
    private String identityCard;
    private String phone;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String delFlag;
    private String lockFlag;
    private String wxOpenid;
    private String qqOpenid;
    /**
     * 角色列表
     */
    private List<SysRole> roleList;
    private List<SysOrg> orgList;
}
