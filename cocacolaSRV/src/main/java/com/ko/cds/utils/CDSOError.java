package com.ko.cds.utils;

import java.io.Serializable;

/***
 * 
 * @author IBM
 *
 */
public class CDSOError implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5971720361492709881L;
	private String errorCode;
	private String errorDescription;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public CDSOError() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CDSOError(String errorCode, String errorDescription) {
		super();
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
	

}
