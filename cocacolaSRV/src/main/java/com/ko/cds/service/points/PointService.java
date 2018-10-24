package com.ko.cds.service.points;

import java.math.BigInteger;

import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.points.CreditPointRequest;
import com.ko.cds.request.points.DebitPointRequest;
import com.ko.cds.request.points.PointsBalanceRequest;
import com.ko.cds.request.points.PointsHistoryRequest;
import com.ko.cds.response.points.PointTransResponse;
import com.ko.cds.response.points.PointsBalanceResponse;
import com.ko.cds.response.points.PointsHistoryResponse;
import com.ko.cds.service.helper.PointAccountServiceHelper;
import com.ko.cds.utils.CDSOUtils;

/***
 * This is the implementation class for points API
 * 
 * @author IBM
 * 
 */
@Component
@Transactional

public class PointService implements IPointService {
	@Autowired
	private PointAccountServiceHelper pointAccountServiceHelper;

	@Override
	@AopServiceAnnotation
	public Response creditPointsTransaction(
			CreditPointRequest creditPointRequest) throws BadRequestException {
		PointTransResponse creditPointResponse = new PointTransResponse();
		BigInteger transactionId = pointAccountServiceHelper
				.creditPoint(creditPointRequest);
		creditPointResponse.setTransactionId(transactionId);
		return CDSOUtils.createOKResponse(creditPointResponse);
	}

	public PointAccountServiceHelper getPointAccountServiceHelper() {
		return pointAccountServiceHelper;
	}

	public void setPointAccountServiceHelper(
			PointAccountServiceHelper pointAccountServiceHelper) {
		this.pointAccountServiceHelper = pointAccountServiceHelper;
	}

	@Override
	@AopServiceAnnotation
	public Response debitPointsTransaction(DebitPointRequest debitPointRequest)
			throws BadRequestException {
		PointTransResponse dPointTransResponse = new PointTransResponse();
		BigInteger transactionId = pointAccountServiceHelper.debitPoint(debitPointRequest);
		dPointTransResponse.setTransactionId(transactionId);
		return CDSOUtils.createOKResponse(dPointTransResponse);
	}

	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getPointsBalance(PointsBalanceRequest pointsBalReq)
			throws BadRequestException {
		PointsBalanceResponse pointsbalResp = pointAccountServiceHelper
				.getPointsBalance(pointsBalReq);

		if (pointsbalResp == null) {
			// Empty JSON Object - to create {} as notation for empty resultset
			JSONObject obj = new JSONObject();			
			return CDSOUtils.createOKResponse(obj.toString());
		} else {
			return CDSOUtils.createOKResponse(pointsbalResp);
		}
	}
	
	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getPointsHistory(PointsHistoryRequest pointsHistoryReq)
			throws BadRequestException {
		PointsHistoryResponse pointsHistoryResp = pointAccountServiceHelper
				.getPointsHistory(pointsHistoryReq);

		if (pointsHistoryResp == null) {
			// Empty JSON Object - to create {} as notation for empty resultset
			JSONObject obj = new JSONObject();
			return CDSOUtils.createOKResponse(obj.toString());
		} else {
			return CDSOUtils.createOKResponse(pointsHistoryResp);
		}
	}
	
	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getPointsOldHistory(PointsHistoryRequest pointsHistoryReq)
			throws BadRequestException {
		PointsHistoryResponse pointsHistoryResp = pointAccountServiceHelper
				.getPointsOldHistory(pointsHistoryReq);

		if (pointsHistoryResp == null) {
			// Empty JSON Object - to create {} as notation for empty resultset
			JSONObject obj = new JSONObject();
			return CDSOUtils.createOKResponse(obj.toString());
		} else {
			return CDSOUtils.createOKResponse(pointsHistoryResp);
		}
	}
}
