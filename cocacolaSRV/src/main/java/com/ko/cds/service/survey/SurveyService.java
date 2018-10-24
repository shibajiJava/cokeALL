package com.ko.cds.service.survey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.survey.Survey;
import com.ko.cds.request.survey.GetSurveyRequest;
import com.ko.cds.request.survey.OptsRequest;
import com.ko.cds.request.survey.PostSurveyRequest;
import com.ko.cds.response.survey.GetOptsResponse;
import com.ko.cds.service.helper.SurveyServiceHelper;
import com.ko.cds.utils.CDSOUtils;

@Component
@Transactional
public class SurveyService implements ISurveyService {
	
	@Autowired
	private SurveyDAO surveyDAO;

	@Autowired
	private SessionIdDAO sessionIdDAO;
	
	@Autowired
	private SurveyServiceHelper serviceHelper;

	
	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response postSurvey(PostSurveyRequest postSurveyRequest)
			throws BadRequestException {
		 serviceHelper.postSurvey(postSurveyRequest);
		 return CDSOUtils.createOKResponse(null);
	}
		
	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getSurvey(GetSurveyRequest getSurveyRequest)
			throws BadRequestException {			

		Survey survey = serviceHelper.getSurvey(getSurveyRequest);

		return CDSOUtils.createOKResponse(survey);
	}
	
	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response postOpts(OptsRequest optsRequest)
			throws BadRequestException {
		
		serviceHelper.postOpts(optsRequest);
		
		return CDSOUtils.createOKResponse(null);			
	}
	
	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getOpts(OptsRequest optsRequest) throws BadRequestException {

		GetOptsResponse response = serviceHelper.getOpts(optsRequest);

		// Empty JSON Object - to create {} as notation for empty resultset
		if (response == null) {
			JSONObject obj = new JSONObject();
			return CDSOUtils.createOKResponse(obj.toString());			
		} else {
			return CDSOUtils.createOKResponse(response);
		}
	}
}
