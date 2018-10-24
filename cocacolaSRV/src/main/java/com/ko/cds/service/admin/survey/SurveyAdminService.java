package com.ko.cds.service.admin.survey;

import java.math.BigInteger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.dao.admin.survey.SurveyAdminDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.request.admin.survey.PostSurveyAdminRequest;
import com.ko.cds.request.admin.survey.PostSurveyQuestionAdminRequest;
import com.ko.cds.response.admin.survey.PostSurveyAdminResponse;
import com.ko.cds.response.admin.survey.PostSurveyQuestionAdminResponse;
import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.utils.CDSOUtils;

@Component
@Transactional
public class SurveyAdminService implements ISurveyAdminService {
	
	@Autowired
	private SurveyAdminDAO surveyDAO;

	@Autowired
	private SurveyServiceAdminHelper serviceHelper;

	
	/*Admin Survey Helper Implementation 1-feb-2016*/
	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class,CustomAdminException.class})
	@AopServiceAnnotation
	@Override
	public Response postSurveyAdmin(PostSurveyAdminRequest postSurveyAdminRequest)
			throws CustomAdminException {
		PostSurveyAdminResponse SurveyResponse = new PostSurveyAdminResponse();
		 BigInteger SurveyIDresponse = serviceHelper.postSurveyAdmin(postSurveyAdminRequest);
		 SurveyResponse.setSurveyId(SurveyIDresponse);
		 return CDSOUtils.createOKResponse(SurveyResponse);

	}	 
    @Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class,CustomAdminException.class})
	@AopServiceAnnotation
	@Override
	public Response postSurveyQuestionAdmin(PostSurveyQuestionAdminRequest postSurveyQuestionAdminRequest)
			throws CustomAdminException {
    	PostSurveyQuestionAdminResponse SurveyResponse = new PostSurveyQuestionAdminResponse();
		BigInteger SurveyIDresponse = serviceHelper.postSurveyQuestionAdmin(postSurveyQuestionAdminRequest);
		SurveyResponse.setSurveyId(SurveyIDresponse);
		return CDSOUtils.createOKResponse(SurveyResponse);		
		 	
	}
    /*@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response postQuestionAdmin(PostQuestionAdminRequest postQuestionAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		PostQuestionAdminResponse questionIdResponse = new PostQuestionAdminResponse();
		BigInteger QuestionIdResp = serviceHelper.postQuestionAdmin(postQuestionAdminRequest);
		questionIdResponse.setQuestionID(QuestionIdResp);
		return CDSOUtils.createOKResponse(questionIdResponse);
	}
    @Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response postAnswerAdmin(PostAnswerAdminRequest postAnswerAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		PostAnswerAdminResponse answerIdResponse = new PostAnswerAdminResponse();
		BigInteger answerId = serviceHelper.postAnswerAdmin(postAnswerAdminRequest);
		answerIdResponse.setAnswerID(answerId);
		return CDSOUtils.createOKResponse(answerIdResponse);
	}
    @Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response postReasonAdmin(PostReasonAdminRequest postReasonAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
    	serviceHelper.postReasonAdmin(postReasonAdminRequest);
		return CDSOUtils.createOKResponse(null);
	}
    @Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response postSiteAdmin(PostSiteAdminRequest postSiteAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
    	PostSiteAdminResponse siteIdResponse = new PostSiteAdminResponse();
		BigInteger siteId = serviceHelper.postSiteAdmin(postSiteAdminRequest);
		siteIdResponse.setSiteId(siteId);
		return CDSOUtils.createOKResponse(siteIdResponse);
	}
	@Override
	public Response getAnswerAdmin(GetAnswerAdminRequest getAnswerAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		GetAnswerAdminResponseList resp =new GetAnswerAdminResponseList();
		
		resp = serviceHelper.getAnswerAdmin(getAnswerAdminRequest);
        
		if (resp == null) {
			// Empty JSON Object - to create {} as notation for empty resultset
			JSONObject obj = new JSONObject();			
			return CDSOUtils.createOKResponse(obj.toString());
		} else {
			return CDSOUtils.createOKResponse(resp);
		}
		
	}
	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getQuestionAdmin(GetQuestionAdminRequest getQuestionAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		GetQuestionAdminResponseList resp =new GetQuestionAdminResponseList();
		
		resp = serviceHelper.getQuestionAdmin(getQuestionAdminRequest);
        
		if (resp == null) {
			// Empty JSON Object - to create {} as notation for empty resultset
			JSONObject obj = new JSONObject();			
			return CDSOUtils.createOKResponse(obj.toString());
		} else {
			return CDSOUtils.createOKResponse(resp);
		}
		
	}
	@Override
	public Response getSiteAdmin(GetSiteAdminRequest getSiteAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		GetSiteAdminResponseList resp =new GetSiteAdminResponseList();
		
		resp = serviceHelper.getSiteAdmin(getSiteAdminRequest);
        
		if (resp == null) {
			// Empty JSON Object - to create {} as notation for empty resultset
			JSONObject obj = new JSONObject();			
			return CDSOUtils.createOKResponse(obj.toString());
		} else {
			return CDSOUtils.createOKResponse(resp);
		}
		
	}
	@Override
	public Response updateSiteAdmin(UpdateSiteAdminRequest updateSiteAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		UpdateSiteAdminResponse resp =new UpdateSiteAdminResponse();
		int siteId = serviceHelper.updateSiteAdmin(updateSiteAdminRequest);
		resp.setSiteId(siteId);
		return CDSOUtils.createOKResponse(resp);
		
	}
	@Override
	public Response updateAnswerAdmin(UpdateAnswerAdminRequest updateAnswerAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		UpdateAnswerAdminResponse resp = new UpdateAnswerAdminResponse();
		BigInteger AnswerID = serviceHelper.updateAnswerAdmin(updateAnswerAdminRequest);
	    resp.setAnswerID(AnswerID);
		return CDSOUtils.createOKResponse(resp);
	}
	@Override
	public Response updateQuestionAdmin(UpdateQuestionAdminRequest updateAnswerAdminRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		UpdateQuestionAdminResponse resp =new UpdateQuestionAdminResponse();
		BigInteger QuestionID = serviceHelper.updateQuestionAdmin(updateAnswerAdminRequest);
		resp.setQuestionID(QuestionID);
		return CDSOUtils.createOKResponse(resp);
	}*/
}
