package com.ko.cds.request.question;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import com.ko.cds.pojo.question.Question;



public class PostQuestionRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurvey
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@MaxLength(value=36)
	private String sessionUuid;
	@NotNull
	private BigInteger surveyId;
	@NotNull
	private BigInteger memberId;
	@NotNull
	private BigInteger questionId;
	@NotNull
	private List<Question> questions;
	
	
	public String getSessionUuid() {
		return sessionUuid;
	}
	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
	}
	
	public BigInteger getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(BigInteger surveyId) {
		this.surveyId = surveyId;
	}
	public BigInteger getQuestionId() {
		return questionId;
	}
	public void setQuestionId(BigInteger questionId) {
		this.questionId = questionId;
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
