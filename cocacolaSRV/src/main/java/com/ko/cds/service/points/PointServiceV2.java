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
import com.ko.cds.request.points.PointsBalanceRequestV2;
import com.ko.cds.request.points.PointsHistoryRequest;
import com.ko.cds.response.points.PointTransResponse;
import com.ko.cds.response.points.PointsBalanceResponse;
import com.ko.cds.response.points.PointsBalanceResponseV2;
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

public class PointServiceV2 implements IPointServiceV2 {
	@Autowired
	private PointAccountServiceHelper pointAccountServiceHelper;


	public PointAccountServiceHelper getPointAccountServiceHelper() {
		return pointAccountServiceHelper;
	}

	public void setPointAccountServiceHelper(
			PointAccountServiceHelper pointAccountServiceHelper) {
		this.pointAccountServiceHelper = pointAccountServiceHelper;
	}


	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getPointsBalance(PointsBalanceRequestV2 pointsBalReq)
			throws BadRequestException {
		PointsBalanceResponseV2 pointsbalResp = pointAccountServiceHelper
				.getPointsBalanceV2(pointsBalReq);

		if (pointsbalResp == null) {
			// Empty JSON Object - to create {} as notation for empty resultset
			JSONObject obj = new JSONObject();			
			return CDSOUtils.createOKResponse(obj.toString());
		} else {
			return CDSOUtils.createOKResponse(pointsbalResp);
		}
	}
	
}
