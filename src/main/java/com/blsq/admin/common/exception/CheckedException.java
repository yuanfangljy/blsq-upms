package com.blsq.admin.common.exception;

import lombok.NoArgsConstructor;

/**
 * <p>
 * 类描述：异常处理
 * </p>
 *
 * @author liujiayi
 * @since 2019/4/24 9:54
 * @version：1.0
 */
@NoArgsConstructor
public class CheckedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}