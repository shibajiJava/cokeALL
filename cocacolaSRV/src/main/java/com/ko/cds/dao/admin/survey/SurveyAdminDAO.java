package com.ko.cds.dao.admin.survey;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;






















import com.ko.cds.pojo.admin.answer.AdminAnswer;
import com.ko.cds.pojo.admin.answer.UpdateAdminAnswer;
import com.ko.cds.pojo.admin.question.AdminQuestion;
import com.ko.cds.pojo.admin.question.UpdateAdminQuestion;
import com.ko.cds.pojo.admin.reason.AdminReason;
import com.ko.cds.pojo.admin.site.AdminSite;
import com.ko.cds.pojo.admin.site.UpdateAdminSite;
import com.ko.cds.pojo.admin.survey.QuestionIdAdmin;
import com.ko.cds.pojo.admin.survey.SurveyAdmin;
import com.ko.cds.request.admin.answer.GetAnswerAdminRequest;
import com.ko.cds.request.admin.question.GetQuestionAdminRequest;
import com.ko.cds.request.admin.question.UpdateQuestionAdminRequest;
import com.ko.cds.request.admin.site.GetSiteAdminRequest;
import com.ko.cds.request.admin.survey.QuestionIdS;
import com.ko.cds.response.admin.answer.GetAnswerAdminResponse;
import com.ko.cds.response.admin.question.GetQuestionAdminResponse;
import com.ko.cds.response.admin.site.GetSiteAdminResponse;




@Component
public interface SurveyAdminDAO {
	public void postSurveyAdmin(SurveyAdmin surveyAdmin);
	public void postSurveyQuestionAdmin(List<QuestionIdAdmin> serveyquestionidAdmin);
	public void deleteQuestionIDAdmin(QuestionIdAdmin surveyQuestions);
	public BigInteger getQuestionIdIfAny(QuestionIdAdmin surveyQuestions);
	//public BigInteger getSurveyIdIfAny(BigInteger surveyId);
	public void postNewQuestion(AdminQuestion questiondetails);
	public void postQandAmapping(List<AdminQuestion> listofQuestionanswers);
	public void postAnswersAdmin(AdminAnswer listofanswers);
	public void postReasonAdmin(AdminReason setter);
	public void postSite(AdminSite setter);
	public void updateSiteAdmin(UpdateAdminSite setter);
	public void updateAnswerAdmin(UpdateAdminAnswer setter);
	public void updateQuestionAnswerXref(UpdateAdminQuestion qandAlist);
	public void updateQuestiononly(UpdateAdminQuestion questionupdate);
	public List<GetSiteAdminResponse> getSites(GetSiteAdminRequest getSiteAdminRequest);
	public List<GetAnswerAdminResponse> getAnswers(GetAnswerAdminRequest getAnswerAdminRequest);
	public List<GetQuestionAdminResponse> getQuestion(GetQuestionAdminRequest getQuestionAdminRequest);
	public List<GetQuestionAdminResponse> getQuestions(GetQuestionAdminRequest getQuestionAdminRequest);
	public void updateDisplayOrder(UpdateQuestionAdminRequest updateQuestionAdminRequest);
	
}
