package com.fc.dtc.exception;

/**
 * 自定义序列化异常
 * @author fangyuan
 *
 */
public class SerializationException extends RuntimeException {

	public SerializationException() {
		super();
	}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SerializationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SerializationException(Throwable arg0) {
		super(arg0);
	}
	
	
	
}
