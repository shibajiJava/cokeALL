package com.ko.cds.pojo.admin.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class QuestionAdmin implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger questionId;
	private String questionType;
	private String questionString;
	private String displayType;
	private int displayOrder;
	
	private List<AnswerAdmin> answerOptions;	

	public BigInteger getQuestionId() {
		return questionId;
	}

	public void setQuestionId(BigInteger questionId) {
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

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	

	public List<AnswerAdmin> getAnswerOptions() {
		return answerOptions;
	}

	public void setAnswerOptions(List<AnswerAdmin> answerOptions) {
		this.answerOptions = answerOptions;
	}

	

}
