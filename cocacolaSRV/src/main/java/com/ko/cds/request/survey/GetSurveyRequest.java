package com.ko.cds.request.survey;

import java.math.BigInteger;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

/**
 * Request object for getSurvey API.
 * @author IBM
 *
 */
public class GetSurveyRequest {
	
	@QueryParam("sessionUUID")
	@MaxLength(value = 36)	
	private String sessionUUID;	
	@QueryParam("memberId")
	private BigInteger memberId;
	@QueryParam("surveyId")
	@NotNull
	private BigInteger surveyId;
	
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
	public BigInteger getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(BigInteger surveyId) {
		this.surveyId = surveyId;
	}
		
}
