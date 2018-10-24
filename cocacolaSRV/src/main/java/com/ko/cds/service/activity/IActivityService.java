package com.ko.cds.service.activity;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.activity.BunchBallActivityRequest;
import com.ko.cds.request.activity.RecordRedemptionRequest;
import com.ko.cds.request.activity.RecordSessionInfoRequest;

/**
 * Service interface for Activity APIs to handle REST request
 * 
 * @author IBM
 * 
 */

@Component
@Path("/cds/v1")
public interface IActivityService {

	/**
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/recordSession Method to
	 * record session info.
	 * 
	 * @param recordSessionInfoRequest
	 */
	@POST
	@Path("/session/record")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response recordSessionInfo(
			RecordSessionInfoRequest recordSessionInfoRequest) throws BadRequestException;
	
	@POST
	@Path("/activity")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response bunchBall(BunchBallActivityRequest bunchBallActivityRequest) throws BadRequestException;
	
	@POST
	@Path("/redeemedCodeTransaction")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response recordRedemption(RecordRedemptionRequest recordRedemptionRequest) throws BadRequestException;
	
	
}
