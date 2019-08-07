package com.blsq.admin.common.vo;

import lombok.Data;

/**
 * @author liujiayi
 * @date 2019/22/31
 * 前端日志vo
 */
@Data
public class PreLogVo {
	private String url;
	private String time;
	private String user;
	private String type;
	private String message;
	private String stack;
	private String info;
}
