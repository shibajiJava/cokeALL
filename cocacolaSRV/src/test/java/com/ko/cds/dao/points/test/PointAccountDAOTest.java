package com.ko.cds.dao.points.test;

import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.pojo.common.SequenceNumber;
import com.ko.cds.pojo.points.AccountBalancePK;
import com.ko.cds.pojo.points.PointAccountBalance;
import com.ko.cds.pojo.points.TransactionHistory;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;

@Component
@Transactional
public class PointAccountDAOTest extends CDSTest{
	private static Logger log = LoggerFactory.getLogger(PointAccountDAOTest.class);
	
	
	 @Test
	 public void testPointAccountInsert(){
		  //
		    BigInteger memberId = createMember();
		    PointAccountBalance pointBalanceAccount = new PointAccountBalance();
			pointBalanceAccount.setPointAccountType("MCR");
			pointBalanceAccount.setMemberId(memberId);
			pointBalanceAccount.setPointBalance(10);
			pointBalanceAccount.setWeekCreditQty(10);
			pointBalanceAccount.setDailyCreditQty(10);
			pointBalanceAccount.setYearlyCreditQty(10);
			pointAccountDAO.insertAccountBalance(pointBalanceAccount);
			// Fetch record
			AccountBalancePK accountBalancePK = new AccountBalancePK();
			accountBalancePK.setMemberId(memberId);
			accountBalancePK.setPointAccountType("MCR");
		    PointAccountBalance pointBalanceAccount1= pointAccountDAO.getAccountDetails(accountBalancePK);
		    Assert.assertEquals(memberId, pointBalanceAccount1.getMemberId());
		    Assert.assertEquals(10, pointBalanceAccount1.getPointBalance());
		    Assert.assertEquals(10, pointBalanceAccount1.getWeekCreditQty());
		    Assert.assertEquals(10, pointBalanceAccount1.getDailyCreditQty());
		    Assert.assertEquals(10, pointBalanceAccount1.getYearlyCreditQty());
		 
	 }
	 
	 @Test
	 public void testPointAccountUpdate(){
		    BigInteger memberId = createMember();
		    PointAccountBalance pointBalanceAccount = new PointAccountBalance();
			pointBalanceAccount.setPointAccountType("MCR");
			pointBalanceAccount.setMemberId(memberId);
			pointBalanceAccount.setPointBalance(10);
			pointBalanceAccount.setWeekCreditQty(10);
			pointBalanceAccount.setDailyCreditQty(10);
			pointBalanceAccount.setYearlyCreditQty(10);
			pointAccountDAO.insertAccountBalance(pointBalanceAccount);
			AccountBalancePK accountBalancePK = new AccountBalancePK();
			accountBalancePK.setMemberId(memberId);
			accountBalancePK.setPointAccountType("MCR");
		   PointAccountBalance pointBalanceAccount1= pointAccountDAO.getAccountDetails(accountBalancePK);
		  
		   pointBalanceAccount1.setCreditOrDebitType(CDSConstants.CREDIT_TYPE_REGULAR);
		   pointBalanceAccount1.setPointBalance(pointBalanceAccount1.getPointBalance()+10);
			pointBalanceAccount1.setDailyCreditQty(pointBalanceAccount1.getDailyCreditQty()+10);
			pointBalanceAccount1.setWeekCreditQty(pointBalanceAccount1.getWeekCreditQty()+10);
			pointBalanceAccount1.setYearlyCreditQty(pointBalanceAccount1.getYearlyCreditQty()+10);
		    pointAccountDAO.updateAccountBalance(pointBalanceAccount1);
		    
		    PointAccountBalance pointBalanceAccount2= pointAccountDAO.getAccountDetails(accountBalancePK);
		    Assert.assertEquals(memberId, pointBalanceAccount.getMemberId());
		    Assert.assertEquals(20, pointBalanceAccount2.getPointBalance());
		    Assert.assertEquals(20, pointBalanceAccount2.getWeekCreditQty());
		    Assert.assertEquals(20, pointBalanceAccount2.getDailyCreditQty());
		    Assert.assertEquals(20, pointBalanceAccount2.getYearlyCreditQty());
		    
	 }
	 @Test
	 public void testPointTransInsert(){
		 TransactionHistory history = new TransactionHistory();
			history.setAccountType("MCR");
			history.setRelatedTransactionId(BigInteger.valueOf(123459));
			history.setClientTransId("13145");
			//Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
			
			//history.setTransactionDate(new Timestamp(calendar.getTimeInMillis()));
			history.setTransactionTimeStampString(sequenceNumberDAO.getCurrentDBTime());
			history.setPointsQuanity(10);
			history.setPointsSource("bunchball");
			history.setReasonCode("1");
			history.setTransactionType(CDSConstants.TRANS_TYPE_CREDIT);
			history.setPointsType(CDSConstants.CREDIT_TYPE_REGULAR);
			history.setPromotionId(null);
			history.setStakeHolderId(null);
			history.setProductID("123");
			BigInteger memberId= createMember();
			history.setMemberId(memberId);
			SequenceNumber sequenceNumber = new SequenceNumber(CDSConstants.TRANS_ID_SEQ);
			BigInteger transactionId=sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
			history.setPointTransId(transactionId);
			history.setSessionId(BigInteger.valueOf(2));
			history.setItemId(123);
		    pointAccountDAO.insertTransactionHistory(history);
		    TransactionHistory updatedHistory= pointAccountDAO.getPointHistory(transactionId);
		    Assert.assertEquals(history.getHoldTime(), updatedHistory.getHoldTime());
		    Assert.assertEquals(history.getPointsType(),updatedHistory.getPointsType());
		    Assert.assertEquals(history.getSessionId(), updatedHistory.getSessionId());
		    Assert.assertEquals(history.getItemId(), updatedHistory.getItemId());
	 }
	 
	 @Test
	 public void testPointTransUpdate(){
		 TransactionHistory history = new TransactionHistory();
			history.setAccountType("MCR");
			history.setRelatedTransactionId(BigInteger.valueOf(123459));
			history.setClientTransId("13145");
			//Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
			
			//history.setTransactionDate(new Timestamp(calendar.getTimeInMillis()));
			history.setTransactionTimeStampString(sequenceNumberDAO.getCurrentDBTime());
			history.setPointsQuanity(10);
			history.setPointsSource("bunchball");
			history.setReasonCode("1");
			history.setTransactionType(CDSConstants.TRANS_TYPE_DEBIT);
			history.setPointsType(CDSConstants.DEBIT_TYPE_HOLD);
			history.setPromotionId(null);
			history.setStakeHolderId(null);
			history.setProductID("123");
			history.setHoldTime(240);
			BigInteger memberId= createMember();
			history.setMemberId(memberId);
			SequenceNumber sequenceNumber = new SequenceNumber(CDSConstants.TRANS_ID_SEQ);
			BigInteger transactionId=sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
			history.setPointTransId(transactionId);
			history.setSessionId(BigInteger.valueOf(2));
		    pointAccountDAO.insertTransactionHistory(history);
		    history.setHoldTime(null);
		    history.setPointsType(CDSConstants.DEBIT_TYPE_CONFIRM);
		    history.setSessionId(null);
		    pointAccountDAO.updatePointHistory(history);
		    TransactionHistory updatedHistory= pointAccountDAO.getPointHistory(transactionId);
		    Assert.assertEquals(history.getHoldTime(), updatedHistory.getHoldTime());
		    Assert.assertEquals(history.getPointsType(),updatedHistory.getPointsType());
		    Assert.assertEquals(history.getSessionId(), updatedHistory.getSessionId());
		    
	 }
	 
   
   
}
