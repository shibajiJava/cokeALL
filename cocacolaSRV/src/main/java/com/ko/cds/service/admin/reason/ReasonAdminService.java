package com.ko.cds.service.admin.reason;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.dao.admin.survey.SurveyAdminDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.request.admin.reason.PostReasonAdminRequest;
import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.utils.CDSOUtils;

@Component
@Transactional
public class ReasonAdminService implements IReasonAdminService {
	
	@Autowired
	private SurveyAdminDAO surveyDAO;

	@Autowired
	private SurveyServiceAdminHelper serviceHelper;


    @Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class,CustomAdminException.class})
	@AopServiceAnnotation
	@Override
	public Response postReasonAdmin(PostReasonAdminRequest postReasonAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
    	serviceHelper.postReasonAdmin(postReasonAdminRequest);
		return CDSOUtils.createOKResponse(null);
	}
   
}
