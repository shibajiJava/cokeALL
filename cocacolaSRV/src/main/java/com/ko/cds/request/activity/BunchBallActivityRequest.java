package com.ko.cds.request.activity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

import com.ko.cds.pojo.activity.TagObject;

public class BunchBallActivityRequest implements Serializable{

	/**
	 * BunchBallActivityRequest Pojo
	 *  
	 * @author IBM
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//@NotNull
	@Length(min=1,message = "member.create.violation")
	private String sessionUUID;
	
	@NotNull
	@Length(min=1,message = "member.create.violation")
	private BigInteger memberId;
	/*@NotNull
	@Length(min=1,max=4)
	private String bevProdCd;*/
	@AssertValid
	@NotNull
	@Length(min=1,message = "member.create.violation")
	private List<TagObject> tag;
	
	
	public String getSessionUUID() {
		return sessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	/*
	public String getBevProdCd() {
		return bevProdCd;
	}
	public void setBevProdCd(String bevProdCd) {
		this.bevProdCd = bevProdCd;
	}*/
	public List<TagObject> getTag() {
		return tag;
	}
	public void setTag(List<TagObject> tag) {
		this.tag = tag;
	}
}
