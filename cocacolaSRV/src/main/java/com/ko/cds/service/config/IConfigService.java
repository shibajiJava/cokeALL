package com.ko.cds.service.config;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.BadRequestException;



@Component
@Path("/cds/v1")
public interface IConfigService {

	
	/**
	 * getReasonCodes service.
	 * 
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/config/reason-codes")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getReasonCodes()throws BadRequestException;

	
}
