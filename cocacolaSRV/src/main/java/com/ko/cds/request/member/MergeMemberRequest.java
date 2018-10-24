package com.ko.cds.request.member;

import java.io.Serializable;
import java.math.BigInteger;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author IBM
 *
 */
public class MergeMemberRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 190989787L;
	
	/**Unique session ID created by TCCC*/
	private String sessionUUID;
	/**The Unique member ID of the account that is getting removed and all points transferred to the “to” member account */

	@NotNull
	@Length(min=1,message = "member.create.violation")
	private BigInteger fromMemberId;
	/**The Unique member ID of the account that is going to gain the points, and member information from the “from” member account .*/
	@NotNull
	@Length(min=1,message = "member.create.violation")
	private BigInteger toMemberId;
	
	/**If the value “Y” is selected here then the member will have a “bonus” merge performed Else only Regular points will be merged.*/
	@NotNull(message="member.create.violation")
	@JsonProperty()
	@MinLength(value = 1)
	private String Bonus;
	
	
	public String getBonus() {
		return Bonus;
	}
	public void setBonus(String bonus) {
		Bonus = bonus;
	}
	public String getSessionUUID() {
		return sessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}
	public BigInteger getFromMemberId() {
		return fromMemberId;
	}
	public void setFromMemberId(BigInteger fromMemberId) {
		this.fromMemberId = fromMemberId;
	}
	public BigInteger getToMemberId() {
		return toMemberId;
	}
	public void setToMemberId(BigInteger toMemberId) {
		this.toMemberId = toMemberId;
	}
	
}
