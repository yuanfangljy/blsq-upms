package com.blsq.admin.common.constant;

/**
 * @author liujiayi
 * @version：1.0
 * @since 2018/12/27 10:05
 */
public interface CommonConstants {
	/**
	 * header 中租户ID
	 */
	String TENANT_ID = "TENANT_ID";
	/**
	 * 删除
	 */
	String STATUS_DEL = "1";
	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单
	 */
	String MENU = "0";

	/**
	 * 超级管理员
	 */
	String SUPER_ADMIN="ROLE_SUPER";

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "blsq-ui";

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "blsq";

	/**
	 * 路由存放
	 */
	String ROUTE_KEY = "gateway_route_key";

	/**
	 * spring boot admin 事件key
	 */
	String EVENT_KEY = "event_key";

	/**
	 * 验证码前缀
	 */
	String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_";

	/**
	 * 图片地址
	 */
	String PICTURE_ADDRESS="https://img.kklie.com:9443/images/static";

	/**
	 * 查询所有小区列表
	 */
	String HOUSE_RUL="http://39.108.101.235:8002/blwy/region/names";
	/**
	 * 系统，白名单-黑名单
	 */
	String INTERFACE_LIST="blsq_sys_interface_list";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 0;
	/**
	 * 失败标记
	 */
	Integer FAIL = 1;
	/**
	 * 父类Id
	 */
	Integer PARENT_ID = -1;

	/**
	 * 默认存储bucket
	 */
	String BUCKET_NAME = "lengleng";
}
