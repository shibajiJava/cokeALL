package com.ko.cds.exceptions;

public class CustomAdminException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1261421498753623811L;
	private String errorCode;
	public CustomAdminException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomAdminException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public CustomAdminException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CustomAdminException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CustomAdminException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public CustomAdminException(String message,String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	public CustomAdminException(String message,String errorCode,Throwable cause) {
		super(message,cause);
		this.errorCode = errorCode;
		
	}
	

}
