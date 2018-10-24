package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/***
 * 
 * @author IBM
 *
 */
@JsonIgnoreProperties(value={"memberId","channelTypeCode","domainName","userId","userName","statusCode","insertDt","updateDt","memberIndList"})
public class MemberIdentifier implements Serializable {
	public static final String BLNK_STR=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** memberid **/
	private BigInteger memberId;
	@NotNull
	@MaxLength(value=256)
	private String name;
	@NotNull
	@MaxLength(value=256)
	private String value; 
	
	/** Domain Name**/
	private String domainName=BLNK_STR;
	/** Channel Type Code**/
	private String channelTypeCode=BLNK_STR;
	
	/** User Id**/
	private String userId=BLNK_STR;
	/** User Name **/
	private String userName=BLNK_STR;
	@MatchPattern(pattern="Active|InActive",message="member.create.violation" )
	/** Status Code **/
	private String statusCode="Active";
	
	private Date insertDt;
	
	private Date updateDt;
	
	private List<MemberIdentifier> memberIndList;
	
	public List<MemberIdentifier> getMemberIndList() {
		return memberIndList;
	}
	public void setMemberIndList(List<MemberIdentifier> memberIndList) {
		this.memberIndList = memberIndList;
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
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
		
	}
	public String getChannelTypeCode() {
		return channelTypeCode;
	}
	public void setChannelTypeCode(String channelTypeCode) {
		this.channelTypeCode = channelTypeCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getName() {
		this.name=this.domainName;
		return name;
	}
	public void setName(String name) {
		this.domainName=name;
		this.name = name;
	}
	public String getValue() {
		this.value=this.userId;
		return value;
	}
	public void setValue(String value) {
		this.userId=value;
		//this.userName=value;
		this.value = value;
	}
	
	

}
