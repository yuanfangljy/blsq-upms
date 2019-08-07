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
 *//*


package com.blsq.admin.common.auth;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

*/
/**
 * <p>
 * 类描述：参数设置
 * </p>
 *
 * @author liujiayi
 * @since 2019/5/28 14:17
 * @version：1.0
 *//*


@Data
@Configuration
@ConfigurationProperties(prefix = "security.jwt.auth-parameters")
public class AuthParameters {
    private String jwtTokenSecret;
    private long tokenExpiredMs;
}

*/
