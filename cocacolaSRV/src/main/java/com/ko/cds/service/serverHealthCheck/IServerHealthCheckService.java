package com.ko.cds.service.serverHealthCheck;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.BadRequestException;


@Component

@Path("/cds/v1")
public interface IServerHealthCheckService {
	
	
	/**
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/serverhealthcheck
	 * @return
	 * @throws BadRequestException
	 */
	@Path("/serverhealthcheck")
	@GET
	@Produces({MediaType.APPLICATION_JSON}) 	
	public Response serverHealthCheck() throws BadRequestException;
	
	
}
