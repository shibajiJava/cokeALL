package com.ko.cds.request.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.jvnet.hk2.annotations.Optional;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import com.ko.cds.pojo.survey.Question;



public class PostSurveyRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurvey
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@MaxLength(value=36)
	private String sessionUUID;
	@NotNull
	@Length(min=1)
	private BigInteger surveyId;
	@NotNull
	@Length(min=1)
	private BigInteger memberId;
	
	@AssertValid
	private List<Question> questions;
	
	/**
	 * new chages for Survey 
	 * 01/22/2016
	 */
	
	
	@MaxLength(value = 36, message = "survey.postUUID.violation")
	private String clientTransactionId;
	
	public String getClientTransactionId() {
		return clientTransactionId;
	}
	public void setClientTransactionId(String clientTransactionId) {
		this.clientTransactionId = clientTransactionId;
	}
	public String getSessionUUID() {
		return sessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}
	public BigInteger getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(BigInteger surveyId) {
		this.surveyId = surveyId;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
