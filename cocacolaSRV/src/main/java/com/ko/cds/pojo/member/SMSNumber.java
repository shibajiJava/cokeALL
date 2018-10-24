package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import net.sf.oval.constraint.MaxLength;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/***
 * 
 * @author IBM
 *
 */
@JsonIgnoreProperties(value={"memberId","alias","insertDt","updateDt","statusCode","validationCode"})
public class SMSNumber implements Serializable {
	public static final String BLNK_STR=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@MaxLength(value = 19, message = "member.create.violation")
	/** SMS Number **/
	private BigInteger smsNumber;
	/** member id*/
	private BigInteger memberId;
	/** Status Code**/
	private String statusCode="Active";
	@MaxLength(value = 10, message = "member.create.violation")
	private String smsType;
	private String alias;
	
	private String validationCode;

	private Date insertDt;
	
	private Date updateDt;
	
	
	
	public String getValidationCode() {
		return validationCode;
	}
	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Date getInsertDt() {
		return insertDt;
	}
	public void setInsertDt(Date insertDt) {
		this.insertDt = insertDt;
	}
	public Date getUpdateDt() {
		return updateDt;
	}
	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}
	
	public BigInteger getSmsNumber() {
		return smsNumber;
	}
	public void setSmsNumber(BigInteger smsNumber) {
		this.smsNumber = smsNumber;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SMSNumber [smsNumber=" + smsNumber + ", memberId=" + memberId
				+ ", statusCode=" + statusCode + ", smsType=" + smsType
				+ ", alias=" + alias + ", insertDt=" + insertDt + ", updateDt="
				+ updateDt + "]";
	}
	
	

}
