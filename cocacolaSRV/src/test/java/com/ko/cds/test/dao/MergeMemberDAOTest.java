package com.ko.cds.test.dao;


import java.math.BigInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.AssertThrows;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.member.MergeMemberRequest;
import com.ko.cds.service.helper.MemberServiceHelper;
import com.ko.cds.service.helper.PointAccountServiceHelper;
import com.ko.cds.test.CDSTest;


@Component
@Transactional
public class MergeMemberDAOTest extends CDSTest{
	public static final Logger logger=LoggerFactory.getLogger(MergeMemberDAOTest.class);
	
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private MemberServiceHelper memberServiceHelper;
	@Autowired
	private PointAccountServiceHelper pointAccountServiceHelper;
	@Autowired
    private PointAccountDAO pointAccountDAO;
	

	
	@Test
	@Rollback(true)
	public void testMergeTransAndPointsLiteAccToFull() throws BadRequestException{
		MergeMemberRequest mergeMemberRequest=new MergeMemberRequest();
		mergeMemberRequest.setFromMemberId(new BigInteger("342"));
		mergeMemberRequest.setToMemberId(new BigInteger("337"));
		BigInteger toMemberId=memberServiceHelper.mergeLiteMemberDetails(mergeMemberRequest);
		logger.info("Your new Member ID is : " , toMemberId);
	}

	//When UUID is same then Error Code: NOT_MERGED_NONLITE_ACCOUNT will be throw.	
	@Test(expected=BadRequestException.class)
	@Rollback(true)
	public void testMergeMemberFullAccUUID() throws BadRequestException {

		MergeMemberRequest mergeMemberRequest=new MergeMemberRequest();
		mergeMemberRequest.setFromMemberId(new BigInteger("337"));
		mergeMemberRequest.setToMemberId(new BigInteger("337"));
		BigInteger toMemberId=memberServiceHelper.mergeLiteMemberDetails(mergeMemberRequest);
		logger.info("Your new Member ID is : "  +toMemberId);
	}
	
	
	
	@Test
	@Rollback(true)
	public void testMergeEmailMember() throws BadRequestException{

		MergeMemberRequest mergeMemberRequest=new MergeMemberRequest();
		mergeMemberRequest.setFromMemberId(new BigInteger("338"));
		mergeMemberRequest.setToMemberId(new BigInteger("337"));
		BigInteger toMemberId=memberServiceHelper.mergeLiteMemberDetails(mergeMemberRequest);
		logger.info("Your new Member ID is : "  +toMemberId);
	}
	
	@Test
	@Rollback(true)
	public void testMergeMemberIdentifier() throws BadRequestException {
		MergeMemberRequest mergeMemberRequest=new MergeMemberRequest();
		mergeMemberRequest.setFromMemberId(new BigInteger("340"));
		mergeMemberRequest.setToMemberId(new BigInteger("337"));
		BigInteger toMemberId=memberServiceHelper.mergeLiteMemberDetails(mergeMemberRequest);
		logger.info("Your new Member ID is : "  +toMemberId);
	}
	
	@Test
	@Rollback(true)
	public void testMergeOptsToMember() throws BadRequestException {		

		MergeMemberRequest mergeMemberRequest=new MergeMemberRequest();
		mergeMemberRequest.setFromMemberId(new BigInteger("341"));
		mergeMemberRequest.setToMemberId(new BigInteger("337"));
		BigInteger toMemberId=memberServiceHelper.mergeLiteMemberDetails(mergeMemberRequest);
		logger.info("Your new Member ID is : "  +toMemberId);
	}
	
	@Test
	@Rollback(true)
	public void testMergeSMSMember() throws BadRequestException {		
		MergeMemberRequest mergeMemberRequest=new MergeMemberRequest();
		mergeMemberRequest.setFromMemberId(new BigInteger("336"));
		mergeMemberRequest.setToMemberId(new BigInteger("337"));
		BigInteger toMemberId=memberServiceHelper.mergeLiteMemberDetails(mergeMemberRequest);
		logger.info("Your new Member ID is : "  +toMemberId);
	}
	
	
}
