package com.ko.cds.exceptions;

public class BadRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7142085425206909300L;
	private String errorCode;
	public BadRequestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public BadRequestException(String message,String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	public BadRequestException(String message,String errorCode,Throwable cause) {
		super(message,cause);
		this.errorCode = errorCode;
		
	}
	

}
