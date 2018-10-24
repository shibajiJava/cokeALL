package com.ko.cds.request.csr;

import java.io.Serializable;
import java.math.BigInteger;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;
/***
 * 
 * This class is used to hold converted JSON structure into POJO for 
 * CSR Ticket GET reuest
 * @author ibm
 *
 */

public class CSRTicketGetRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8182062677263455355L;
	@QueryParam("sessionUUID")
	/** The unique session ID */
	@MaxLength(value = 36, message = "ticket.create.violation")
	private String sessionUUID;
	/**Unique Id for the member generated by CDS*/
	@QueryParam("memberId")
	@NotNull
	private BigInteger memberId;
	
	private BigInteger sessionId;
	
	public BigInteger getSessionId() {
		return sessionId;
	}
	public void setSessionId(BigInteger sessionId) {
		this.sessionId = sessionId;
	}
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
	
	

}
