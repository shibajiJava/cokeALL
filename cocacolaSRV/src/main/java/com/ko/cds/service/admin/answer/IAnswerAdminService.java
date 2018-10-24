package com.ko.cds.service.admin.answer;

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
import com.ko.cds.request.admin.answer.GetAnswerAdminRequest;
import com.ko.cds.request.admin.answer.PostAnswerAdminRequest;
import com.ko.cds.request.admin.answer.UpdateAnswerAdminRequest;


@Component
@Path("/cds/v1")
public interface IAnswerAdminService {

	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/admin/answer
	 * 
	 * @param postSurveyAdmin
	 * @return
	 * @throws CustomAdminException
	 * 
	 */

	@POST
	@Path("/admin/answer")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response postAnswerAdmin(
			PostAnswerAdminRequest postAnswerAdminRequest)
			throws CustomAdminException;

	@GET
	@Path("/admin/answer")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAnswerAdmin(GetAnswerAdminRequest getAnswerAdminRequest)
			throws CustomAdminException;

	@PUT
	@Path("/admin/answer")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateAnswerAdmin(
			UpdateAnswerAdminRequest updateAnswerAdminRequest)
			throws CustomAdminException;

}
