package com.ko.cds.service.admin.question;

import java.math.BigInteger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.dao.admin.survey.SurveyAdminDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.request.admin.question.GetQuestionAdminRequest;
import com.ko.cds.request.admin.question.PostQuestionAdminRequest;
import com.ko.cds.request.admin.question.UpdateQuestionAdminRequest;
import com.ko.cds.response.admin.question.GetQuestionAdminResponseList;
import com.ko.cds.response.admin.question.PostQuestionAdminResponse;
import com.ko.cds.response.admin.question.UpdateQuestionAdminResponse;
import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.utils.CDSOUtils;

@Component
@Transactional
public class QuestionAdminService implements IQuestionAdminService {
	
	@Autowired
	private SurveyAdminDAO surveyDAO;

	@Autowired
	private SurveyServiceAdminHelper serviceHelper;

	

    @Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response postQuestionAdmin(PostQuestionAdminRequest postQuestionAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		PostQuestionAdminResponse questionIdResponse = new PostQuestionAdminResponse();
		BigInteger QuestionIdResp = serviceHelper.postQuestionAdmin(postQuestionAdminRequest);
		questionIdResponse.setQuestionID(QuestionIdResp);
		return CDSOUtils.createOKResponse(questionIdResponse);
	}
   
	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getQuestionAdmin(GetQuestionAdminRequest getQuestionAdminRequest)
			throws CustomAdminException {
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
	public Response updateQuestionAdmin(UpdateQuestionAdminRequest updateAnswerAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		UpdateQuestionAdminResponse resp =new UpdateQuestionAdminResponse();
		BigInteger QuestionID = serviceHelper.updateQuestionAdmin(updateAnswerAdminRequest);
		resp.setQuestionID(QuestionID);
		return CDSOUtils.createOKResponse(resp);
	}
}
