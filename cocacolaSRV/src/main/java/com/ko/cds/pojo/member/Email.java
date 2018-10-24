package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

/***
 * 
 * @author IBM
 *
 */
public class Email implements Serializable{
	public static final String BLNK_STR="";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*** member id*/
	private BigInteger memberId;
	/**email address **/
	private String email=BLNK_STR;
	
	/** status code **/
	private String statusCode="Active";
	/** primary indicator **/
	private String primaryIndicator="Y";
	
	private String validInd = BLNK_STR;
	
	private Timestamp emaiVerifiedDt;
	
	
	
	public String getValidInd() {
		return validInd;
	}
	public void setValidInd(String validInd) {
		this.validInd = validInd;
	}
	public Timestamp getEmaiVerifiedDt() {
		return emaiVerifiedDt;
	}
	public void setEmaiVerifiedDt(Timestamp emaiVerifiedDt) {
		this.emaiVerifiedDt = emaiVerifiedDt;
	}
	public String getEmail() {
		return email;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getPrimaryIndicator() {
		return primaryIndicator;
	}
	public void setPrimaryIndicator(String primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}
	

@Override 


public String toString() { 


	StringBuffer sb=new StringBuffer(" email : ").append(this.email);
	sb.append("\n memberId :");
	sb.append(memberId);

   return sb.toString();


  } 


}
