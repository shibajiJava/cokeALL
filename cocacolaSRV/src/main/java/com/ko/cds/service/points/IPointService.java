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
import com.ko.cds.request.points.PointsHistoryRequest;

/**
 * 
 * @author IBM
 *
 */
@Component

@Path("/cds/v1")
public interface IPointService {
	
	
	/**
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/points/credit
	 * @param creditPointRequest
	 * @return
	 * @throws BadRequestException
	 */
	@POST
	@Path("/points/credit")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON}) 		
	public Response creditPointsTransaction(CreditPointRequest creditPointRequest) throws BadRequestException;
	
	/**
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/points/debit
	 * @param debitPointRequest
	 * @return
	 * @throws BadRequestException
	 */
	@POST
	@Path("/points/debit")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON}) 		
	public Response debitPointsTransaction(DebitPointRequest debitPointRequest) throws BadRequestException;
	
	/**
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/points/balance
	 * @param pointsBalReq
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/points/balance")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPointsBalance(@BeanParam PointsBalanceRequest pointsBalReq) throws BadRequestException;
	
	/**
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/points/history
	 * @param pointsHistoryReq
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/points/history")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPointsHistory(@BeanParam PointsHistoryRequest pointsHistoryReq) throws BadRequestException;

	/**
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/points/oldhistory
	 * @param pointsHistoryReq
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/points/oldhistory")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPointsOldHistory(@BeanParam PointsHistoryRequest pointsHistoryReq) throws BadRequestException;

}
