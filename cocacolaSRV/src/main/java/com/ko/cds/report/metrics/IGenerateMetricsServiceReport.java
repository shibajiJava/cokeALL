package com.ko.cds.report.metrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.BadRequestException;

@Component
@Path("/cds/v1")
public interface IGenerateMetricsServiceReport {

	@GET
	@Path("/metricsReport")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response generateReport(@QueryParam("startTime")String startTime , @QueryParam("endTime")String endTime) throws BadRequestException;
}
