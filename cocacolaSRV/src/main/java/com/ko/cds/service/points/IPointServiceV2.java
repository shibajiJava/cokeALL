package com.ko.cds.service.points;

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
import com.ko.cds.request.points.CreditPointRequest;
import com.ko.cds.request.points.DebitPointRequest;
import com.ko.cds.request.points.PointsBalanceRequest;
import com.ko.cds.request.points.PointsBalanceRequestV2;
import com.ko.cds.request.points.PointsHistoryRequest;

/**
 * 
 * @author IBM
 *
 */
@Component

@Path("/cds/v2")
public interface IPointServiceV2 {
	/**
	 * http://localhost:8080/cocacolaSRV/rest/cds/v2/points/balance
	 * @param pointsBalReq
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/points/balance")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPointsBalance(@BeanParam PointsBalanceRequestV2 pointsBalReq) throws BadRequestException;

}
