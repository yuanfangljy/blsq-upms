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

package com.blsq.admin.common.config;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.model.SysInterfaceList;
import com.blsq.admin.service.ISysInterfaceListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

*/
/**
 * <p>
 * 类描述：系统，白名单-黑名单 初始化
 * </p>
 *
 * @author liujiayi
 * @since 2019/6/3 11:23
 * @version：1.0
 *//*

@Slf4j
@Configuration
@AllArgsConstructor
public class InterfaceListConfig {

    private final RedisTemplate blsqRedisTemplate;
    private final ISysInterfaceListService sysInterfaceListService;

    @Async
    @Order
    @EventListener({WebServerInitializedEvent.class})
    public void initInterfaceList(){
        Boolean result = blsqRedisTemplate.delete(CommonConstants.INTERFACE_LIST);
        log.info("初始化系统：白黑-名单接口",result);
        List<SysInterfaceList> interfaceLists = sysInterfaceListService.list(new QueryWrapper<SysInterfaceList>().lambda().eq(SysInterfaceList::getDelFlag, CommonConstants.SUCCESS));
        interfaceLists.forEach(interfaceList->{
            JSONArray filterObj = JSONUtil.parseArray(interfaceList);
            log.info("加载接口名单：{},{}", interfaceList.getUrl(), filterObj);
            blsqRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(SysInterfaceList.class));
            blsqRedisTemplate.opsForHash().put(CommonConstants.INTERFACE_LIST, interfaceList.getId(), interfaceList);
        });
        log.debug("加载接口名单结束 ");
    }
}
*/
