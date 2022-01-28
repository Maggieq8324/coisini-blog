package com.coisini.exception;

/**
 * @Description 业务异常
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = 4126134436273565941L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
