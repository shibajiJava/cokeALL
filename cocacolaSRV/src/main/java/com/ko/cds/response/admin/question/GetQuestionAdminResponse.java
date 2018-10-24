package com.ko.cds.response.admin.question;

import java.io.Serializable;


import java.math.BigInteger;
import java.util.List;

import com.ko.cds.response.admin.answer.GetAnswerAdminResponse;


public class GetQuestionAdminResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private int questionId;
	
	private String questionType;
	
	private String questionString;
	
	/*private int displayOrder; */
	
	private String displayType;

	
	private int displayOrder; 
	private List<GetAnswerAdminResponse> answerOptions;


	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public List<GetAnswerAdminResponse> getAnswerOptions() {
		return answerOptions;
	}
	public void setAnswerOptions(List<GetAnswerAdminResponse> answerOptions) {
		this.answerOptions = answerOptions;
	}


	public int getQuestionId() {
		return questionId;
	}



	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}



	public String getQuestionType() {
		return questionType;
	}



	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}



	public String getQuestionString() {
		return questionString;
	}



	public void setQuestionString(String questionString) {
		this.questionString = questionString;
	}



/*	public int getDisplayOrder() {
		return displayOrder;
	}



	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
*/


	public String getDisplayType() {
		return displayType;
	}



	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}



	@Override
	public String toString() {
		return "displayType [questionId=" +questionId+", questionType"+questionType+", questionString"+questionString+", displayType"+displayType+"]";
	}

	
	
	
}
