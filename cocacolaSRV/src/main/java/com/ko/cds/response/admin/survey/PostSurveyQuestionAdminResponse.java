package com.ko.cds.response.admin.survey;

import java.io.Serializable;


import java.math.BigInteger;


public class PostSurveyQuestionAdminResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private BigInteger surveyId;

	public BigInteger getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(BigInteger surveyId) {
		this.surveyId = surveyId;
	}

	
	
	
}
