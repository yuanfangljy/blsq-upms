package com.blsq.admin.common.util;

import com.blsq.admin.common.constant.CommonConstants;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author liujiayi
 * @version：1.0
 * @since 2018/12/27 10:05
 */
@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private int code = CommonConstants.SUCCESS;

	@Getter
	@Setter
	private String message = "success";


	@Getter
	@Setter
	private T data;

	public R() {
		super();
	}

	public R(T data) {
		super();
		this.data = data;
	}

	public R(T data, String message) {
		super();
		this.data = data;
		this.message = message;
	}

	public R(Integer code,T data, String message) {
		super();
		this.code=code;
		this.data = data;
		this.message = message;
	}
	public R(Throwable e) {
		super();
		this.message = e.getMessage();
		this.code = CommonConstants.FAIL;
	}
}
