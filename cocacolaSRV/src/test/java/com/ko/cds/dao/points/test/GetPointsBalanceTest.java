package com.ko.cds.dao.points.test;

import java.math.BigInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.request.points.PointsBalanceRequest;
import com.ko.cds.response.points.PointsBalanceResponse;
import com.ko.cds.test.CDSTest;

/**
 * Juints for Points Balance api.
 * @author IBM
 *
 */
public class GetPointsBalanceTest extends CDSTest {

	private static Logger log = LoggerFactory
			.getLogger(GetPointsBalanceTest.class);
	@Autowired
	private PointAccountDAO pointAccountDAO;

	@Test
	public void getPointsBalance() {
		PointsBalanceRequest balReq = new PointsBalanceRequest();
		balReq.setMemberId(new BigInteger("85"));
		balReq.setAccountType("MCR");

		PointsBalanceResponse balResp = pointAccountDAO
				.getPointsBalance(balReq);
		log.info("PointsBalanceResponse : " + balResp);
		Assert.notNull(balResp);
		System.out.print("details : ");
		System.out.println("WRegbal : " + balResp.getWeeklyRegularBalance());
		System.out.println("WLimit : " + balResp.getWeeklyRegularLimit());
		System.out.println("Exp : " + balResp.getPointsExpirationDate());
		System.out.println("total : " + balResp.getTotalBalance());
		System.out.println("WeekTotal : " + balResp.getWeeklyBalanceTotal());
	}
}
