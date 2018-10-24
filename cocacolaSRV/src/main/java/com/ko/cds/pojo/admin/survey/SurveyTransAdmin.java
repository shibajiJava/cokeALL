package com.ko.cds.pojo.admin.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

public class SurveyTransAdmin implements Serializable{

	/**
	 * @author IBM
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@MaxLength(value=32)
	private BigInteger sessionId;
	@NotNull
	private BigInteger surveyId;
	@NotNull	
	private BigInteger memberId;
	@NotNull
	private BigInteger questionId;
	@NotNull
	private BigInteger answerId; 
	@NotNull	
	private Timestamp surveyDt;
	
	private Timestamp insertDt;
	
	/**
	 * New changes  for Survey 
	 * Date 01/22/2016
	 */
	private String textAnswer; 
	
	public String getTextAnswer() {
		return textAnswer;
	}
	public void setTextAnswer(String textAnswer) {
		this.textAnswer = textAnswer;
	}
	
	
	private String clientTransactionId;
	
	public String getClientTransactionId() {
		return clientTransactionId;
	}
	public void setClientTransactionId(String clientTransactionId) {
		this.clientTransactionId = clientTransactionId;
	}
	
	
	
	public BigInteger getSessionId() {
		return sessionId;
	}
	public void setSessionId(BigInteger sessionId) {
		this.sessionId = sessionId;
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
	
	
	public BigInteger getQuestionId() {
		return questionId;
	}
	public void setQuestionId(BigInteger questionId) {
		this.questionId = questionId;
	}
	public BigInteger getAnswerId() {
		return answerId;
	}
	public void setAnswerId(BigInteger answerId) {
		this.answerId = answerId;
	}
	public Timestamp getSurveyDt() {
		return surveyDt;
	}
	public void setSurveyDt(Timestamp surveyDt) {
		this.surveyDt = surveyDt;
	}
	public Timestamp getInsertDt() {
		return insertDt;
	}
	public void setInsertDt(Timestamp insertDt) {
		this.insertDt = insertDt;
	}
	
}
