package com.blsq.admin.common.constant;

/**
 * <p>
 * 类描述： 响应给前端的状态码（在抛出异常的时候使用）
 * </p>
 *
 * @author liujiayi
 * @version：1.0
 * @since 2019/1/17 16:55
 */
public interface ResponseConstant {

    /**
     * 478: 验证码错误，请重新输入
     */
    public static final int SC_VERIFICATION_CODE_FAIL = 478;
    /**
     * 479: 演示环境，没有权限操作
     */
    public static final int SC_THE_DEMO_ENVIRONMENT = 479;
    /**
     * 401: 当前操作没有权限
     */
    public static final int SC_BAD_REQUEST = 400;
    /**
     * 401: 当前操作没有权限
     */
    public static final int SC_UNAUTHORIZED = 401;
    /**
     * 403: 当前操作没有权限
     */
    public static final int SC_FORBIDDEN = 403;
    /**
     * 402: 请求头中信息为空
     */
    public static final int SC_PAYMENT_REQUIRED = 402;
    /**
     * 900: 账号被禁用，登录失败，请联系管理员
     */
    public static final int SC_FORBIDDEN_FAIL = 900;
    /**
     * 901: 账号被锁定，登录失败，请联系管理员
     */
    public static final int SC_LOCKED_IN_FAIL = 901;
    /**
     * 478: 登录成功
     */
    public static final int SC_LOGIN_SUCCESS = 200;

}
