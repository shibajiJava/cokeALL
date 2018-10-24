package com.ko.cds.service.points.test;

import java.math.BigInteger;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.points.AccountBalancePK;
import com.ko.cds.pojo.points.PointAccountBalance;
import com.ko.cds.pojo.points.PointsHistory;
import com.ko.cds.pojo.points.TransactionHistory;
import com.ko.cds.request.points.CreditPointRequest;
import com.ko.cds.request.points.DebitPointRequest;
import com.ko.cds.request.points.PointsHistoryRequest;
import com.ko.cds.service.helper.PointAccountServiceHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.ErrorCode;

@Component
@Transactional
public class PointServiceHelperTest extends CDSTest {
	private static Logger log = LoggerFactory.getLogger(PointServiceHelperTest.class);
	@Autowired
	private PointAccountServiceHelper serviceHelper;
	@Test
	public void testCreditForRegularCreditTypeWithNoInitialPointBalance(){
		BigInteger transactionId= null;
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_REGULAR);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		AccountBalancePK accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		PointAccountBalance pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getPointBalance());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
		// fetch pointHistory details
		TransactionHistory transactionHistory=pointAccountDAO.getPointHistory(transactionId);
		Assert.assertEquals("MCR", transactionHistory.getAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
		Assert.assertEquals(creditRequest.getReasonCode(), transactionHistory.getReasonCode());
		Assert.assertEquals(CDSConstants.TRANS_TYPE_CREDIT, transactionHistory.getTransactionType());
		Assert.assertEquals(creditRequest.getCreditType(), transactionHistory.getPointsType());
		Assert.assertEquals(creditRequest.getRelatedTransactionId(), transactionHistory.getRelatedTransactionId());
		Assert.assertEquals(creditRequest.getClientTransactionId(), transactionHistory.getClientTransId());
		Assert.assertEquals(creditRequest.getPromotion(), transactionHistory.getPromotionId());
		Assert.assertEquals(creditRequest.getMemberId(), transactionHistory.getMemberId());
		Assert.assertEquals(creditRequest.getProductId(), transactionHistory.getProductID());
	}
	@Test
	public void testCreditForBonusCreditTypeWithNoInitialPointBalance(){
		BigInteger transactionId= null;
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_BONUS);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		AccountBalancePK accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		PointAccountBalance pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getPointBalance());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekCreditQty());
		// fetch pointHistory details
		TransactionHistory transactionHistory=pointAccountDAO.getPointHistory(transactionId);
		Assert.assertEquals("MCR", transactionHistory.getAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
		Assert.assertEquals(creditRequest.getReasonCode(), transactionHistory.getReasonCode());
		Assert.assertEquals(CDSConstants.TRANS_TYPE_CREDIT, transactionHistory.getTransactionType());
		Assert.assertEquals(creditRequest.getCreditType(), transactionHistory.getPointsType());
		Assert.assertEquals(creditRequest.getRelatedTransactionId(), transactionHistory.getRelatedTransactionId());
		Assert.assertEquals(creditRequest.getClientTransactionId(), transactionHistory.getClientTransId());
		Assert.assertEquals(creditRequest.getPromotion(), transactionHistory.getPromotionId());
		Assert.assertEquals(creditRequest.getMemberId(), transactionHistory.getMemberId());
		Assert.assertEquals(creditRequest.getProductId(), transactionHistory.getProductID());
	}
	@Test
	public void testCreditForAdminCreditTypeWithNoInitialPointBalance(){
		BigInteger transactionId= null;
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_ADMIN);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		AccountBalancePK accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		PointAccountBalance pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getPointBalance());
		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekCreditQty());
		// fetch pointHistory details
		TransactionHistory transactionHistory=pointAccountDAO.getPointHistory(transactionId);
		Assert.assertEquals("MCR", transactionHistory.getAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
		Assert.assertEquals(creditRequest.getReasonCode(), transactionHistory.getReasonCode());
		Assert.assertEquals(CDSConstants.TRANS_TYPE_CREDIT, transactionHistory.getTransactionType());
		Assert.assertEquals(creditRequest.getCreditType(), transactionHistory.getPointsType());
		Assert.assertEquals(creditRequest.getRelatedTransactionId(), transactionHistory.getRelatedTransactionId());
		Assert.assertEquals(creditRequest.getClientTransactionId(), transactionHistory.getClientTransId());
		Assert.assertEquals(creditRequest.getPromotion(), transactionHistory.getPromotionId());
		Assert.assertEquals(creditRequest.getMemberId(), transactionHistory.getMemberId());
		Assert.assertEquals(creditRequest.getProductId(), transactionHistory.getProductID());
	}
	@Test
	public void testCreditForRegularCreditTypeWithInitialPointBalance(){
		BigInteger transactionId= null;
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_REGULAR);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		AccountBalancePK accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		PointAccountBalance pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getPointBalance());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
		
		// Update
		creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_REGULAR);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getPointBalance());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getWeekCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
		// fetch pointHistory details
	    TransactionHistory transactionHistory=pointAccountDAO.getPointHistory(transactionId);
		Assert.assertEquals("MCR", transactionHistory.getAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
		Assert.assertEquals(creditRequest.getReasonCode(), transactionHistory.getReasonCode());
		Assert.assertEquals(CDSConstants.TRANS_TYPE_CREDIT, transactionHistory.getTransactionType());
		Assert.assertEquals(creditRequest.getCreditType(), transactionHistory.getPointsType());
		Assert.assertEquals(creditRequest.getRelatedTransactionId(), transactionHistory.getRelatedTransactionId());
		Assert.assertEquals(creditRequest.getClientTransactionId(), transactionHistory.getClientTransId());
		Assert.assertEquals(creditRequest.getPromotion(), transactionHistory.getPromotionId());
		Assert.assertEquals(creditRequest.getMemberId(), transactionHistory.getMemberId());
		Assert.assertEquals(creditRequest.getProductId(), transactionHistory.getProductID());
	}
	@Test
	public void testCreditForBonusCreditTypeWithInitialPointBalance(){
		BigInteger transactionId= null;
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_BONUS);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		AccountBalancePK accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		PointAccountBalance pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getPointBalance());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekCreditQty());
		creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_BONUS);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getPointBalance());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getWeekBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekCreditQty());
		// fetch pointHistory details
		TransactionHistory transactionHistory=pointAccountDAO.getPointHistory(transactionId);
		Assert.assertEquals("MCR", transactionHistory.getAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
		Assert.assertEquals(creditRequest.getReasonCode(), transactionHistory.getReasonCode());
		Assert.assertEquals(CDSConstants.TRANS_TYPE_CREDIT, transactionHistory.getTransactionType());
		Assert.assertEquals(creditRequest.getCreditType(), transactionHistory.getPointsType());
		Assert.assertEquals(creditRequest.getRelatedTransactionId(), transactionHistory.getRelatedTransactionId());
		Assert.assertEquals(creditRequest.getClientTransactionId(), transactionHistory.getClientTransId());
		Assert.assertEquals(creditRequest.getPromotion(), transactionHistory.getPromotionId());
		Assert.assertEquals(creditRequest.getMemberId(), transactionHistory.getMemberId());
		Assert.assertEquals(creditRequest.getProductId(), transactionHistory.getProductID());
	}
	@Test
	public void testCreditForAdminCreditTypeWithInitialPointBalance(){
		BigInteger transactionId= null;
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_ADMIN);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		AccountBalancePK accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		PointAccountBalance pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getPointBalance());
		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekCreditQty());
		creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_ADMIN);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getPointBalance());
		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity()+10, pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekCreditQty());
		// fetch pointHistory details
		TransactionHistory transactionHistory=pointAccountDAO.getPointHistory(transactionId);
		Assert.assertEquals("MCR", transactionHistory.getAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
		Assert.assertEquals(creditRequest.getReasonCode(), transactionHistory.getReasonCode());
		Assert.assertEquals(CDSConstants.TRANS_TYPE_CREDIT, transactionHistory.getTransactionType());
		Assert.assertEquals(creditRequest.getCreditType(), transactionHistory.getPointsType());
		Assert.assertEquals(creditRequest.getRelatedTransactionId(), transactionHistory.getRelatedTransactionId());
		Assert.assertEquals(creditRequest.getClientTransactionId(), transactionHistory.getClientTransId());
		Assert.assertEquals(creditRequest.getPromotion(), transactionHistory.getPromotionId());
		Assert.assertEquals(creditRequest.getMemberId(), transactionHistory.getMemberId());
		Assert.assertEquals(creditRequest.getProductId(), transactionHistory.getProductID());
	}
	@Test(expected=BadRequestException.class)
	public void testDailyCreditLimitForRegularCreditTypeWithNoInitialPointBalance() throws BadRequestException{
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(101);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_REGULAR);
		
		serviceHelper.creditPoint(creditRequest);
	
		
	}
	
    @Test(expected=BadRequestException.class)
	public void testDailyCreditLimitForRegularCreditTypeWithInitialPointBalance() throws BadRequestException{
		
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_REGULAR);
		
		serviceHelper.creditPoint(creditRequest);
		
		creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(200);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_REGULAR);
		serviceHelper.creditPoint(creditRequest);
		
	}
    
    @Test(expected=BadRequestException.class)
	public void testDailyBonusCreditLimitForBonusCreditTypeWithNoInitialPointBalance() throws BadRequestException{
		
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(200);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_BONUS);
		
		serviceHelper.creditPoint(creditRequest);
		
		
	}
    
    @Test(expected=BadRequestException.class)
	public void testDailyBonusCreditLimitForBonusCreditTypeWithInitialPointBalance() throws BadRequestException{
		
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_BONUS);
		
		serviceHelper.creditPoint(creditRequest);
		
		creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(200);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_BONUS);
		serviceHelper.creditPoint(creditRequest);
		
	}
    @Test(expected=BadRequestException.class)
	public void testAdminYearlyCreditLimitForAdminCreditTypeWithNoInitialPointBalance() throws BadRequestException{
		
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10000);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_ADMIN);
		
		serviceHelper.creditPoint(creditRequest);
		
		
	}
    @Test(expected=BadRequestException.class)
	public void testYearlyCreditLimitForAdminCreditTypeWithInitialPointBalance() throws BadRequestException{
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(10);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_BONUS);
		
		serviceHelper.creditPoint(creditRequest);
		
		creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(3000);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_BONUS);
		serviceHelper.creditPoint(creditRequest);
		
	}
    @Test
	public void testDebitForRegularDebitType(){
		BigInteger transactionId= null;
		//Create new member
		BigInteger memberId=createMember();
		//Create creditRequest
		CreditPointRequest creditRequest= new CreditPointRequest();
		creditRequest.setMemberId(memberId);
		creditRequest.setAccountType("MCR");
		creditRequest.setPointsQuantity(100);
		creditRequest.setReasonCode("1");
		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_REGULAR);
		try {
			transactionId=serviceHelper.creditPoint(creditRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		AccountBalancePK accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		PointAccountBalance pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getPointBalance());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
		
		// debit 
		DebitPointRequest debitRequest= new DebitPointRequest();
		debitRequest.setMemberId(memberId);
		debitRequest.setAccountType("MCR");
		debitRequest.setPointsQuantity(10);
		debitRequest.setReasonCode("1");
		debitRequest.setDebitType(CDSConstants.DEBIT_TYPE_REGULAR);
		try {
			transactionId=serviceHelper.debitPoint(debitRequest);
		} catch (BadRequestException e) {
			log.info("PointServiceHelperTest :"+e);
		}
		accountBalancePK = new AccountBalancePK(); 
		accountBalancePK.setMemberId(memberId);
		accountBalancePK.setPointAccountType("MCR");
		pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
		Assert.assertEquals(creditRequest.getPointsQuantity()-10, pointAccountBalance.getPointBalance());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyCreditQty());
		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekCreditQty());
		//Assert.assertEquals(creditRequest.get, pointAccountBalance.getYearlyCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
		// fetch pointHistory details
	    TransactionHistory transactionHistory=pointAccountDAO.getPointHistory(transactionId);
		Assert.assertEquals("MCR", transactionHistory.getAccountType());
		Assert.assertEquals(debitRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
		Assert.assertEquals(debitRequest.getReasonCode(), transactionHistory.getReasonCode());
		Assert.assertEquals(CDSConstants.TRANS_TYPE_DEBIT, transactionHistory.getTransactionType());
		Assert.assertEquals(debitRequest.getDebitType(), transactionHistory.getPointsType());
		Assert.assertEquals(debitRequest.getRelatedTransactionId(), transactionHistory.getRelatedTransactionId());
		Assert.assertEquals(debitRequest.getClientTransactionId(), transactionHistory.getClientTransId());
		Assert.assertEquals(debitRequest.getPromotion(), transactionHistory.getPromotionId());
		Assert.assertEquals(debitRequest.getMemberId(), transactionHistory.getMemberId());
		Assert.assertEquals(debitRequest.getProductId(), transactionHistory.getProductID());
	}
    
    @Test
   	public void testHoldAndConfirm(){
   		BigInteger transactionId= null;
   		//Create new member
   		BigInteger memberId=createMember();
   		//Create creditRequest
   		CreditPointRequest creditRequest= new CreditPointRequest();
   		creditRequest.setMemberId(memberId);
   		creditRequest.setAccountType("MCR");
   		creditRequest.setPointsQuantity(100);
   		creditRequest.setReasonCode("1");
   		creditRequest.setCreditType(CDSConstants.CREDIT_TYPE_REGULAR);
   		try {
   			transactionId=serviceHelper.creditPoint(creditRequest);
   		} catch (BadRequestException e) {
   			log.info("PointServiceHelperTest :"+e);
   		}
   		AccountBalancePK accountBalancePK = new AccountBalancePK(); 
   		accountBalancePK.setMemberId(memberId);
   		accountBalancePK.setPointAccountType("MCR");
   		PointAccountBalance pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
   		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getPointBalance());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyCreditQty());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekCreditQty());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
   		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
   		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
   		
   		// debit 
   		DebitPointRequest debitRequest= new DebitPointRequest();
   		debitRequest.setMemberId(memberId);
   		debitRequest.setAccountType("MCR");
   		debitRequest.setPointsQuantity(10);
   		debitRequest.setReasonCode("1");
   		debitRequest.setDebitType(CDSConstants.DEBIT_TYPE_HOLD);
   		try {
   			transactionId=serviceHelper.debitPoint(debitRequest);
   		} catch (BadRequestException e) {
   			log.info("PointServiceHelperTest :"+e);
   		}
   		accountBalancePK = new AccountBalancePK(); 
   		accountBalancePK.setMemberId(memberId);
   		accountBalancePK.setPointAccountType("MCR");
   		pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
   		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
   		Assert.assertEquals(creditRequest.getPointsQuantity()-10, pointAccountBalance.getPointBalance());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyCreditQty());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekCreditQty());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
   		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
   		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
   		// fetch pointHistory details
   	    TransactionHistory transactionHistory=pointAccountDAO.getPointHistory(transactionId);
   		Assert.assertEquals("MCR", transactionHistory.getAccountType());
   		Assert.assertEquals(debitRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
   		Assert.assertEquals(debitRequest.getReasonCode(), transactionHistory.getReasonCode());
   		Assert.assertEquals(CDSConstants.TRANS_TYPE_DEBIT, transactionHistory.getTransactionType());
   		Assert.assertEquals(debitRequest.getDebitType(), transactionHistory.getPointsType());
   		Assert.assertEquals(null, transactionHistory.getRelatedTransactionId());
   		Assert.assertEquals(debitRequest.getClientTransactionId(), transactionHistory.getClientTransId());
   		Assert.assertEquals(debitRequest.getPromotion(), transactionHistory.getPromotionId());
   		Assert.assertEquals(debitRequest.getMemberId(), transactionHistory.getMemberId());
   		Assert.assertEquals(debitRequest.getProductId(), transactionHistory.getProductID());
   		Assert.assertEquals(new Integer(240), transactionHistory.getHoldTime());
   		Assert.assertEquals(CDSConstants.DEBIT_TYPE_HOLD, transactionHistory.getPointsType());
   		
   	   //Confirm transaction
   		debitRequest= new DebitPointRequest();
   		debitRequest.setMemberId(memberId);
   		debitRequest.setAccountType("MCR");
   		debitRequest.setPointsQuantity(10);
   		debitRequest.setReasonCode("1");
   		debitRequest.setDebitType(CDSConstants.DEBIT_TYPE_CONFIRM);
   		debitRequest.setRelatedTransactionId(transactionId);
   		try {
   			transactionId=serviceHelper.debitPoint(debitRequest);
   		} catch (BadRequestException e) {
   			log.info("PointServiceHelperTest :"+e);
   		}
   		accountBalancePK = new AccountBalancePK(); 
   		accountBalancePK.setMemberId(memberId);
   		accountBalancePK.setPointAccountType("MCR");
   		pointAccountBalance=pointAccountDAO.getAccountDetails(accountBalancePK);
   		Assert.assertEquals("MCR", pointAccountBalance.getPointAccountType());
   		Assert.assertEquals(creditRequest.getPointsQuantity()-10, pointAccountBalance.getPointBalance());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getDailyCreditQty());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getWeekCreditQty());
   		Assert.assertEquals(creditRequest.getPointsQuantity(), pointAccountBalance.getYearlyCreditQty());
   		Assert.assertEquals(0, pointAccountBalance.getDailyBonusCreditQty());
   		Assert.assertEquals(0, pointAccountBalance.getWeekBonusCreditQty());
   	// fetch pointHistory details
   	    transactionHistory=pointAccountDAO.getPointHistory(transactionId);
   		Assert.assertEquals("MCR", transactionHistory.getAccountType());
   		Assert.assertEquals(debitRequest.getPointsQuantity(), transactionHistory.getPointsQuanity());
   		Assert.assertEquals(debitRequest.getReasonCode(), transactionHistory.getReasonCode());
   		Assert.assertEquals(CDSConstants.TRANS_TYPE_DEBIT, transactionHistory.getTransactionType());
   		Assert.assertEquals(debitRequest.getDebitType(), transactionHistory.getPointsType());
   		Assert.assertEquals(null, transactionHistory.getRelatedTransactionId());
   		Assert.assertEquals(debitRequest.getClientTransactionId(), transactionHistory.getClientTransId());
   		Assert.assertEquals(debitRequest.getPromotion(), transactionHistory.getPromotionId());
   		Assert.assertEquals(debitRequest.getMemberId(), transactionHistory.getMemberId());
   		Assert.assertEquals(debitRequest.getProductId(), transactionHistory.getProductID());
   		Assert.assertEquals(CDSConstants.DEBIT_TYPE_CONFIRM, transactionHistory.getPointsType());
   		Assert.assertEquals(null, transactionHistory.getHoldTime());
   		
   		
   	}
    
   
}
