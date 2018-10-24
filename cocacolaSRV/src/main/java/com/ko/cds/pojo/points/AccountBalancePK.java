package com.ko.cds.pojo.points;

import java.io.Serializable;
import java.math.BigInteger;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

public class AccountBalancePK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5669204319183596085L;
	@NotNull
	@Length(min=1)
	private BigInteger memberId;
	@NotNull
	@Length(min=1)
	private String pointAccountType;
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getPointAccountType() {
		return pointAccountType;
	}
	public void setPointAccountType(String pointAccountType) {
		this.pointAccountType = pointAccountType;
	}
	

}
