package com.ko.cds.pojo.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties("markedAnswers")
public class Answer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger optionId;
	private String optionString;	
	private String isSelectedAnswer = "false";
	private int answerOrder;
	
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
	/*
	private String defaultInd;*/
	private List<SurveyTrans> markedAnswers;
		
	public BigInteger getOptionId() {
		return optionId;
	}
	public void setOptionId(BigInteger optionId) {
		this.optionId = optionId;
	}
	public String getOptionString() {
		return optionString;
	}
	public void setOptionString(String optionString) {
		this.optionString = optionString;
	}
	public String getIsSelectedAnswer() {
		return isSelectedAnswer;
	}
	public void setIsSelectedAnswer(String isSelectedAnswer) {
		this.isSelectedAnswer = isSelectedAnswer;
	}	
	/*public int getAnswerOrder() {
		return answerOrder;
	}
	public void setAnswerOrder(int answerOrder) {
		this.answerOrder = answerOrder;
	}
	public String getDefaultInd() {
		return defaultInd;
	}
	public void setDefaultInd(String defaultInd) {
		this.defaultInd = defaultInd;
	}*/
	public List<SurveyTrans> getMarkedAnswers() {
		return markedAnswers;
	}
	public void setMarkedAnswers(List<SurveyTrans> markedAnswers) {
		this.markedAnswers = markedAnswers;
	}
	
	public int getAnswerOrder() {
		return answerOrder;
	}
	public void setAnswerOrder(int answerOrder) {
		this.answerOrder = answerOrder;
	}
}
