package com.ko.cds.service.admin.reason;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.request.admin.reason.PostReasonAdminRequest;
//import com.ko.cds.pojo.survey.SurveyInfo;
//import com.ko.cds.request.survey.GetSurveyRequest;

@Component
@Path("/cds/v1")
public interface IReasonAdminService {


	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/admin/reason
	 * 
	 * @param postReasonAdmin
	 * @return
	 * @throws CustomAdminException
	 * 
	 */

	
	@POST
	@Path("/admin/reason")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response postReasonAdmin(PostReasonAdminRequest postAnswerAdminRequest)
			throws CustomAdminException;
	
	
}
