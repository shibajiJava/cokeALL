package com.ko.cds.response.member;

import java.math.BigInteger;

/**
 * 
 * @author IBM
 *
 */
public class MergeMemberResponse {
	/**Member Id of the merged account*/
	private BigInteger memberId;

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	
	

}
