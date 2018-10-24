package com.ko.cds.service.survey;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.survey.GetSurveyRequest;
import com.ko.cds.request.survey.OptsRequest;
//import com.ko.cds.pojo.survey.SurveyInfo;
//import com.ko.cds.request.survey.GetSurveyRequest;
import com.ko.cds.request.survey.PostSurveyRequest;

@Component
@Path("/cds/v1")
public interface ISurveyService {

	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/survey
	 * 
	 * @param postSurvey
	 * @return
	 * @throws BadRequestException
	 * 
	 */

	@POST
	@Path("/survey")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response postSurvey(PostSurveyRequest postSurveyRequest)
			throws BadRequestException;

	/**
	 * getSurvey service.
	 * 
	 * @param getSurveyRequest
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/survey")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSurvey(@BeanParam GetSurveyRequest getSurveyRequest)
			throws BadRequestException;

	/**
	 * postOpts service.
	 * 
	 * @param optsRequest
	 * @return
	 * @throws BadRequestException
	 */
	@POST
	@Path("/opts")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response postOpts(OptsRequest optsRequest)
			throws BadRequestException;

	/**
	 * getOpts service.
	 * 
	 * @param optsRequest
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/opts")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOpts(@BeanParam OptsRequest optsRequest) throws BadRequestException;
}
