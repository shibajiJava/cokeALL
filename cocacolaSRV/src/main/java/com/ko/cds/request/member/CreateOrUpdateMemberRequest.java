package com.ko.cds.request.member;

import com.ko.cds.pojo.member.MemberInfo;

/***
 * 
 * @author IBM
 *
 */
public class CreateOrUpdateMemberRequest {
	/*** The TCCC Session Id*/
	private String sessionId;
	/**Member Info Object*/
	private MemberInfo memberInfoObject;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public MemberInfo getMemberInfoObject() {
		return memberInfoObject;
	}
	public void setMemberInfoObject(MemberInfo memberInfoObject) {
		this.memberInfoObject = memberInfoObject;
	}
	
	
}
