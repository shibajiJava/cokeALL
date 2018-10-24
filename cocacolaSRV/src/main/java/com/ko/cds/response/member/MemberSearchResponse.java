package com.ko.cds.response.member;

import com.ko.cds.pojo.member.MemberInfo;

/***
 * 
 * @author IBM
 *
 */

public class MemberSearchResponse {

	public MemberInfo getMemberInfoObject() {
		return memberInfoObject;
	}

	public void setMemberInfoObject(MemberInfo memberInfoObject) {
		this.memberInfoObject = memberInfoObject;
	}

	/**Member info object */
	private MemberInfo memberInfoObject;
	
}
