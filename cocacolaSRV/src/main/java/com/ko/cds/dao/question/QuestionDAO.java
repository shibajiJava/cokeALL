package com.ko.cds.dao.question;

import org.springframework.stereotype.Component;
import com.ko.cds.pojo.survey.SurveyTrans;

@Component
public interface QuestionDAO {
	public void postQuestion(SurveyTrans surveyTrans);

}
