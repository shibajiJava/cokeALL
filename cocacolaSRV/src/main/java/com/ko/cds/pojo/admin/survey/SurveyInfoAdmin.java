package com.ko.cds.pojo.admin.survey;

import java.io.Serializable;
import java.util.Set;

public class SurveyInfoAdmin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SurveyAdmin survey;
	private Set<QuestionIdAdmin> questions;
//	private List<Answer> answers;
	
	public SurveyAdmin getSurvey() {
		return survey;
	}
	public void setSurvey(SurveyAdmin survey) {
		this.survey = survey;
	}
	public Set<QuestionIdAdmin> getQuestions() {
		return questions;
	}
	public void setQuestions(Set<QuestionIdAdmin> questions) {
		this.questions = questions;
	}
//	public List<Answer> getAnswers() {
//		return answers;
//	}
//	public void setAnswers(List<Answer> answers) {
//		this.answers = answers;
//	}
		
}
