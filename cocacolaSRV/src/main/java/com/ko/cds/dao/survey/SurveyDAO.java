package com.ko.cds.dao.survey;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ko.cds.pojo.member.CommunicationOptIn;
import com.ko.cds.pojo.survey.Survey;
import com.ko.cds.pojo.survey.SurveyTrans;
import com.ko.cds.request.survey.GetSurveyRequest;
import com.ko.cds.request.survey.OptsRequest;
import com.ko.cds.response.survey.GetOptsResponse;



@Component
public interface SurveyDAO {
	public void postSurvey(SurveyTrans surveyTrans);	
	public Survey getSurveyInfo(GetSurveyRequest getSurveyRequest);
	public Survey getSurveyInfoForMember(GetSurveyRequest getSurveyRequest);
	public int postOpts(OptsRequest optsRequest);
	public GetOptsResponse getOpts(OptsRequest optsRequest);
	public List<CommunicationOptIn> getOptsAfterMerge(BigInteger memberId);
	public void insertOpts(CommunicationOptIn communicationOptIn);
	public void updateOpts(CommunicationOptIn communicationOptIn);
	public List<SurveyTrans> getAnsweredSurvey(SurveyTrans surveyTrans);
	public List<SurveyTrans> getAnsweredSurveyWithQuestionId(SurveyTrans surveyTrans);
	public void insertCommunicationOptsJanrain(CommunicationOptIn janrainCommOptList);
	public void updateCommunicationOptsJanrain(CommunicationOptIn janrainCommOptList);
	public void postSurveyList(List<SurveyTrans> surveyTrans);
}
