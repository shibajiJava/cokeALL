package com.ko.cds.dao.points.test;



import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.points.PointsBalanceRequestV2;
import com.ko.cds.response.points.PointsBalanceResponseV2;
import com.ko.cds.service.helper.PointAccountServiceHelper;
import com.ko.cds.test.CDSTest;

/**
 * Juints for Points Balance api.
 * @author IBM
 *
 */
public class GetPointsBalanceV2Test extends CDSTest {

	@Autowired
	private PointAccountDAO pointAccountDAO;
	
	@Autowired
	private PointAccountServiceHelper pointAccountServiceHelper;

	@Test
	public void getPointsBalanceWithMemberId() throws BadRequestException {
		PointsBalanceRequestV2 pointsBalReq = new PointsBalanceRequestV2();
		pointsBalReq.setMemberId(new BigInteger("3"));

		PointsBalanceResponseV2 pointsbalResp = pointAccountServiceHelper
				.getPointsBalanceV2(pointsBalReq);
		Assert.assertNotNull(pointsbalResp);
		Assert.assertTrue(pointsbalResp.getAccountBalaces().size()>0);
	}
	
	@Test
	public void getPointsBalanceWithOutMemberId(){
		PointsBalanceRequestV2 pointsBalReq = new PointsBalanceRequestV2();

		PointsBalanceResponseV2 pointsbalResp;
		try {
			pointsbalResp = pointAccountServiceHelper
					.getPointsBalanceV2(pointsBalReq);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			Assert.assertEquals(e.getMessage(), "com.ko.cds.request.points.PointsBalanceRequestV2.memberId cannot be null ");
		}
	}
	
	@Test
	public void getPointsBalanceInvalidMemberId() {
		PointsBalanceRequestV2 pointsBalReq = new PointsBalanceRequestV2();
		pointsBalReq.setMemberId(new BigInteger("0"));
		PointsBalanceResponseV2 pointsbalResp;
			try {
				pointsbalResp = pointAccountServiceHelper
						.getPointsBalanceV2(pointsBalReq);
			} catch (BadRequestException e) {
				// TODO Auto-generated catch block
				Assert.assertEquals(e.getMessage(), "Invalid request data");
			}
	}
	
	
}
