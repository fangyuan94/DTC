package com.fc.dtc.exception;

/**
 *
 * 项目名：dtc     类名：TranslateException.java
 * 创建人：fangyuan    创建时间：2019年3月18日
 * 描述:<p>自定义类型转换异常类</p>
 * 修改描述：<p></p>
 * @version v1.0
 */
public class TranslateException extends RuntimeException {

	public TranslateException() {
		super();
	}

	public TranslateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TranslateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TranslateException(String message) {
		super(message);
	}

	public TranslateException(Throwable cause) {
		super(cause);
	}

	
}
