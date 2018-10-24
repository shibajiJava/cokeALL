package com.ko.cds.pojo.admin.survey;

import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;

import net.sf.oval.constraint.Length;

public class SurveyQuestionAdminId {
	

    @Length(min=1)
    @NotNull
	private BigInteger surveyId;
    
    private List<QuestionIdAdmin>  questions;
    
    public BigInteger getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(BigInteger surveyId) {
		this.surveyId = surveyId;
	}

	/**
	 * @return the questions
	 */
	public List<QuestionIdAdmin> getQuestions() {
		return questions;
	}

	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(List<QuestionIdAdmin> questions) {
		this.questions = questions;
	}

	

	
}
