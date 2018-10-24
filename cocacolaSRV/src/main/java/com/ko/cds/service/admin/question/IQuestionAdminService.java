package com.ko.cds.service.admin.question;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.request.admin.question.GetQuestionAdminRequest;
import com.ko.cds.request.admin.question.PostQuestionAdminRequest;
import com.ko.cds.request.admin.question.UpdateQuestionAdminRequest;
//import com.ko.cds.pojo.survey.SurveyInfo;
//import com.ko.cds.request.survey.GetSurveyRequest;

@Component
@Path("/cds/v1")
public interface IQuestionAdminService {



	
	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1v
	 * 
	 * @param postSurveyQuestionAdmin
	 * @return
	 * @throws CustomAdminException
	 * 
	 */

	@POST
	@Path("/admin/question")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	public Response postQuestionAdmin(PostQuestionAdminRequest postQuestionAdminRequest)
			throws CustomAdminException;
	
	
	@GET
	@Path("/admin/question")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getQuestionAdmin(@BeanParam GetQuestionAdminRequest getQuestionAdminRequest)
			throws CustomAdminException;
	
		
	@PUT
	@Path("/admin/question")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateQuestionAdmin(UpdateQuestionAdminRequest updateAnswerAdminRequest)
			throws CustomAdminException;
}
