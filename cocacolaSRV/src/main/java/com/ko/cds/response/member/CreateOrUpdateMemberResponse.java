package com.ko.cds.response.member;

import java.math.BigInteger;

/**
 * 
 * @author IBM
 *
 */
public class CreateOrUpdateMemberResponse {
	/**The member ID of the updated or created member */
	private BigInteger memberId;

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	
	
}
