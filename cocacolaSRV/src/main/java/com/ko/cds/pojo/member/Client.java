package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import net.sf.oval.constraint.MaxLength;

public class Client implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 190L;
	
	@MaxLength(value = 100, message = "member.create.violation")
	private String clientId;
	private BigInteger ident;
	@MaxLength(value = 32, message = "member.create.violation")
	private String channel;
	private Date firstLoginDtm;
	private Date lastLoginDtm;
	
	private BigInteger memberId;
	
	private Date insertDtm;
	private Date updateDtm;
	
	public Date getInsertDtm() {
		return insertDtm;
	}
	public void setInsertDtm(Date insertDtm) {
		this.insertDtm = insertDtm;
	}
	public Date getUpdateDtm() {
		return updateDtm;
	}
	public void setUpdateDtm(Date updateDtm) {
		this.updateDtm = updateDtm;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public BigInteger getIdent() {
		return ident;
	}
	public void setIdent(BigInteger ident) {
		this.ident = ident;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Date getFirstLoginDtm() {
		return firstLoginDtm;
	}
	public void setFirstLoginDtm(Date firstLoginDtm) {
		this.firstLoginDtm = firstLoginDtm;
	}
	public Date getLastLoginDtm() {
		return lastLoginDtm;
	}
	public void setLastLoginDtm(Date lastLoginDtm) {
		this.lastLoginDtm = lastLoginDtm;
	}
	
	
	
}
