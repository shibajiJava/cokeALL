package com.ko.cds.response.survey;

import java.io.Serializable;

import com.ko.cds.pojo.survey.Survey;

/**
 * Response object for getSurvey API.
 * @author IBM
 *
 */
public class GetSurveyResponse implements Serializable {

	/**
	 * serial version id.
	 */
	private static final long serialVersionUID = 1L;

	private Survey survey;

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

}
