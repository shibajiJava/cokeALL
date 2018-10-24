package com.ko.cds.service.admin.site;

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
import com.ko.cds.request.admin.site.GetSiteAdminRequest;
import com.ko.cds.request.admin.site.PostSiteAdminRequest;
import com.ko.cds.request.admin.site.UpdateSiteAdminRequest;
//import com.ko.cds.pojo.survey.SurveyInfo;
//import com.ko.cds.request.survey.GetSurveyRequest;

@Component
@Path("/cds/v1")
public interface ISiteAdminService {


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
	@Path("/admin/site")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	public Response postSiteAdmin(PostSiteAdminRequest postSiteAdminRequest)
			throws CustomAdminException;
	
	
	@GET
	@Path("/admin/site")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSiteAdmin(GetSiteAdminRequest getSiteAdminRequest)
			throws CustomAdminException;
	
	@PUT
	@Path("/admin/site")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateSiteAdmin(UpdateSiteAdminRequest updateSiteAdminRequest)
			throws CustomAdminException;
	
}
