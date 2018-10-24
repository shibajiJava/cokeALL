package com.ko.cds.service.admin.answer;

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
import com.ko.cds.request.admin.answer.GetAnswerAdminRequest;
import com.ko.cds.request.admin.answer.PostAnswerAdminRequest;
import com.ko.cds.request.admin.answer.UpdateAnswerAdminRequest;
import com.ko.cds.response.admin.answer.GetAnswerAdminResponseList;
import com.ko.cds.response.admin.answer.PostAnswerAdminResponse;
import com.ko.cds.response.admin.answer.UpdateAnswerAdminResponse;
import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.utils.CDSOUtils;

@Component
@Transactional
public class AnswerAdminService implements IAnswerAdminService {
	
	@Autowired
	private SurveyAdminDAO surveyDAO;

	@Autowired
	private SurveyServiceAdminHelper serviceHelper;

	

    @Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class,CustomAdminException.class})
	@AopServiceAnnotation
	@Override
	public Response postAnswerAdmin(PostAnswerAdminRequest postAnswerAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		PostAnswerAdminResponse answerIdResponse = new PostAnswerAdminResponse();
		BigInteger answerId = serviceHelper.postAnswerAdmin(postAnswerAdminRequest);
		answerIdResponse.setAnswerID(answerId);
		return CDSOUtils.createOKResponse(answerIdResponse);
	}
   
	@Override
	public Response getAnswerAdmin(GetAnswerAdminRequest getAnswerAdminRequest)
			throws CustomAdminException {
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
	
	@Override
	public Response updateAnswerAdmin(UpdateAnswerAdminRequest updateAnswerAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		UpdateAnswerAdminResponse resp = new UpdateAnswerAdminResponse();
		BigInteger AnswerID = serviceHelper.updateAnswerAdmin(updateAnswerAdminRequest);
	    resp.setAnswerID(AnswerID);
		return CDSOUtils.createOKResponse(resp);
	}
	
}
