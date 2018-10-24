package com.ko.cds.response.member;

import java.util.List;

import com.ko.cds.pojo.member.MemberInfo;

/***
 * 
 * @author IBM
 *
 */

public class MemberSearchResponseV2 {

	/**Member info object */
	private List<MemberInfo> members;

	public List<MemberInfo> getMembers() {
		return members;
	}

	public void setMembers(List<MemberInfo> members) {
		this.members = members;
	}
	
}
