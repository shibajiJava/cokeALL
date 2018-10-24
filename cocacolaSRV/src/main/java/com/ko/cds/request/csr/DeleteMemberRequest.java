package com.ko.cds.request.csr;

import java.math.BigInteger;

import javax.ws.rs.QueryParam;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotNull;

/**
 * Pojo class for Capturing the request for the Delete Member API
 * @author ibm
 *
 */
@JsonIgnoreProperties("sessionId")
public class DeleteMemberRequest {

	@QueryParam("sessionUUID")
	@MaxLength(value=36)
	private String sessionUUID;
	
	@NotNull
	@QueryParam("memberId")
	private BigInteger 	memberId;
	
	@NotNull
	@QueryParam("clientTransactionId")
	@MaxLength(value = 36)
	@MinLength(value = 1)
	private String 	clientTransactionId;
	
	@NotNull
	@QueryParam("deletionReason")
	@MinLength(value = 1)
	@MaxLength(value = 150, message = "member.create.violation")
	private String 	deletionReason;
	
	@NotNull
	@QueryParam("janrainDelete")
	@MaxLength(value = 1)
	@MinLength(value = 1)
	private String	janrainDelete;
	
	private BigInteger sessionId ;
	
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
	public String getClientTransactionId() {
		return clientTransactionId;
	}
	public void setClientTransactionId(String clientTransactionId) {
		this.clientTransactionId = clientTransactionId;
	}
	public String getDeletionReason() {
		return deletionReason;
	}
	public void setDeletionReason(String deletionReason) {
		this.deletionReason = deletionReason;
	}
	
	public String getJanrainDelete() {
		return janrainDelete;
	}
	public void setJanrainDelete(String janrainDelete) {
		this.janrainDelete = janrainDelete;
	}
	
	@Override
	public String toString() {
		return "DeleteMemberRequest [sessionUUID=" + sessionUUID
				+ ", memberId=" + memberId + ", clientTransactionId="
				+ clientTransactionId + ", deletionReason=" + deletionReason
				+ ", janrainDelete=" + janrainDelete + ", sessionId="
				+ sessionId + "]";
	}
	
	
	

	
}
