package com.ko.cds.pojo.activity;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

public class ActivityTrans implements Serializable{

	/**
	 * @category BunchBallActivity
	 * ActivityTrans Pojo
	 * @author IBM
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Length(min=1)
	private BigInteger memberId;
	
	@NotNull
	@Length(min=1,max=50)
	private String name;
	@NotNull
	@MaxLength(value=32)
	private BigInteger sessionId;
	
	@Length(min=1,max=50)	
	private String value;
	/*@NotNull
	private String bevProdCd;*/
	
	private BigInteger transactionID;
	
	private Timestamp insertDtm;
	
	
	
	public Timestamp getInsertDtm() {
		return insertDtm;
	}
	public void setInsertDtm(Timestamp insertDtm) {
		this.insertDtm = insertDtm;
	}
	public BigInteger getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(BigInteger transactionID) {
		this.transactionID = transactionID;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	
	
	
	public BigInteger getSessionId() {
		return sessionId;
	}
	public void setSessionId(BigInteger sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/*
	public String getBevProdCd() {
		return bevProdCd;
	}
	public void setBevProdCd(String bevProdCd) {
		this.bevProdCd = bevProdCd;
	}*/
}
