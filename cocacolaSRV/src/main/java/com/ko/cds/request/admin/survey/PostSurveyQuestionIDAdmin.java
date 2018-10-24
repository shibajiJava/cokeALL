package com.ko.cds.request.admin.survey;

import java.math.BigInteger;

public class PostSurveyQuestionIDAdmin {
	
	
    private BigInteger questionId;
    
    private int displayOrder;
    
    private int minimumAnsLimitQty;
    
    private int maximumAnsLimitQty;
    
    private String questionType;
    
    private String questionText;
    
    private String displayType;
    
    private String requiredInd;

	/**
	 * @return the questionId
	 */
	public BigInteger getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(BigInteger questionId) {
		this.questionId = questionId;
	}

	/**
	 * @return the displayOrder
	 */
	public int getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * @return the minimumAnsLimitQty
	 */
	public int getMinimumAnsLimitQty() {
		return minimumAnsLimitQty;
	}

	/**
	 * @param minimumAnsLimitQty the minimumAnsLimitQty to set
	 */
	public void setMinimumAnsLimitQty(int minimumAnsLimitQty) {
		this.minimumAnsLimitQty = minimumAnsLimitQty;
	}

	/**
	 * @return the maximumAnsLimitQty
	 */
	public int getMaximumAnsLimitQty() {
		return maximumAnsLimitQty;
	}

	/**
	 * @param maximumAnsLimitQty the maximumAnsLimitQty to set
	 */
	public void setMaximumAnsLimitQty(int maximumAnsLimitQty) {
		this.maximumAnsLimitQty = maximumAnsLimitQty;
	}

	/**
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}

	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	/**
	 * @return the displayType
	 */
	public String getDisplayType() {
		return displayType;
	}

	/**
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	/**
	 * @return the requiredInd
	 */
	public String getRequiredInd() {
		return requiredInd;
	}

	/**
	 * @param requiredInd the requiredInd to set
	 */
	public void setRequiredInd(String requiredInd) {
		this.requiredInd = requiredInd;
	}

	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	

}
