package com.ko.cds.service.admin.survey;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.request.admin.survey.PostSurveyAdminRequest;
import com.ko.cds.request.admin.survey.PostSurveyQuestionAdminRequest;
//import com.ko.cds.pojo.survey.SurveyInfo;
//import com.ko.cds.request.survey.GetSurveyRequest;

@Component
@Path("/cds/v1")
public interface ISurveyAdminService {


	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/admin/survey
	 * 
	 * @param postSurveyAdmin
	 * @return
	 * @throws CustomAdminException
	 * 
	 */

	@POST
	@Path("/admin/survey")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	public Response postSurveyAdmin(PostSurveyAdminRequest postSurveyAdminRequest)
			throws CustomAdminException;
	
	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/admin/surveyQuestion
	 * 
	 * @param postSurveyQuestionAdmin
	 * @return
	 * @throws CustomAdminException
	 * 
	 */

	@POST
	@Path("/admin/surveyQuestion")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	public Response postSurveyQuestionAdmin(PostSurveyQuestionAdminRequest postSurveyQuestionAdminRequest)
			throws CustomAdminException;
	
	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/admin/surveyQuestion
	 * 
	 * @param postSurveyQuestionAdmin
	 * @return
	 * @throws BadRequestException
	 * 
	 */

	/*@POST
	@Path("/admin/question")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	public Response postQuestionAdmin(PostQuestionAdminRequest postQuestionAdminRequest)
			throws BadRequestException;
	
	@POST
	@Path("/admin/answer")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	public Response postAnswerAdmin(PostAnswerAdminRequest postAnswerAdminRequest)
			throws BadRequestException;
	
	@POST
	@Path("/admin/reason")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response postReasonAdmin(PostReasonAdminRequest postAnswerAdminRequest)
			throws BadRequestException;
	
	@POST
	@Path("/admin/site")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	public Response postSiteAdmin(PostSiteAdminRequest postSiteAdminRequest)
			throws BadRequestException;
	
	@GET
	@Path("/admin/answer")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAnswerAdmin(GetAnswerAdminRequest getAnswerAdminRequest)
			throws BadRequestException;
	
	@GET
	@Path("/admin/question")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getQuestionAdmin(@BeanParam GetQuestionAdminRequest getQuestionAdminRequest)
			throws BadRequestException;
	
	@GET
	@Path("/admin/site")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSiteAdmin(GetSiteAdminRequest getSiteAdminRequest)
			throws BadRequestException;
	
	@PUT
	@Path("/admin/site")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateSiteAdmin(UpdateSiteAdminRequest updateSiteAdminRequest)
			throws BadRequestException;
	
	@PUT
	@Path("/admin/answer")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateAnswerAdmin(UpdateAnswerAdminRequest updateAnswerAdminRequest)
			throws BadRequestException;
	
	@PUT
	@Path("/admin/question")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateQuestionAdmin(UpdateQuestionAdminRequest updateAnswerAdminRequest)
			throws BadRequestException;*/
}
