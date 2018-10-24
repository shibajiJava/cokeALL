package com.ko.cds.service.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.jmxImpl.JmxConfigurationProperty;
import com.ko.cds.pojo.common.SequenceNumber;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.points.AccountBalance;
import com.ko.cds.pojo.points.AccountBalancePK;
import com.ko.cds.pojo.points.AccountConfiguration;
import com.ko.cds.pojo.points.PointAccountBalance;
import com.ko.cds.pojo.points.PointAccountType;
import com.ko.cds.pojo.points.PointsHistory;
import com.ko.cds.pojo.points.StatusLevels;
import com.ko.cds.pojo.points.TransactionHistory;
import com.ko.cds.request.points.CreditPointRequest;
import com.ko.cds.request.points.DebitPointRequest;
import com.ko.cds.request.points.PointsBalanceRequest;
import com.ko.cds.request.points.PointsBalanceRequestV2;
import com.ko.cds.request.points.PointsHistoryRequest;
import com.ko.cds.response.points.PointsBalanceResponse;
import com.ko.cds.response.points.PointsBalanceResponseV2;
import com.ko.cds.response.points.PointsHistoryResponse;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.CSRConstants;
import com.ko.cds.utils.ErrorCode;
import com.lowagie.text.pdf.ArabicLigaturizer;

@Component
public class PointAccountServiceHelper {

	public static final Logger logger = LoggerFactory
			.getLogger(PointAccountServiceHelper.class);
	@Autowired
	private PointAccountDAO pointAccountDAO;
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private SequenceNumberDAO sequenceNumberDAO;
	@Autowired
	private EhCacheCacheManager cacheManager;
	@Autowired
	private ICacheService cacheService;

	public ICacheService getCacheService() {
		return cacheService;
	}

	public void setCacheService(ICacheService cacheService) {
		this.cacheService = cacheService;
	}

	public BigInteger creditPoint(CreditPointRequest creditPointRequest)
			throws BadRequestException {
		// capture transDT
		// Date transactionDate = CDSOUtils.getCurrentUTCDate();
		String transactionTimeStampString = CDSOUtils.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime());
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(creditPointRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("creditPoint :", e);
			throw new WebApplicationException(e);
		}
		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);

		}

		// 1. get session_id from site_session table using sessionUUID
		long start = System.nanoTime();
		// SessionId will be fetched from Cache
		/*BigInteger sessionId = sessionIdDAO.getSessionID(creditPointRequest
				.getSessionUUID());*/
		BigInteger sessionId = null;
		if(creditPointRequest
				.getSessionUUID() != null){
		   sessionId = cacheService.getSessionID(creditPointRequest
				.getSessionUUID());
		}
		long end = System.nanoTime();
        long elapsedTime = end - start;
        double seconds = (double) elapsedTime / 1000000000.0;
        logger.info("===========Time Taken by getSessionID ============> "+String.valueOf(seconds));
		
		// 2. Check for the sessionId and clientTransactionId in cache
		Cache ehcache = cacheManager
				.getCache(CDSConstants.CLIENT_TRANS_ID_CACHE);
		// if sessionUUID and clientTransId is not null
		String key = null;
		boolean isValidTrans = false;
		//Client transaction Id should be shared across types
		if (creditPointRequest.getClientTransactionId() != null
				&& creditPointRequest.getSessionUUID() == null) {
			StringBuffer sb = new StringBuffer();
			sb.append(creditPointRequest.getClientTransactionId());
			sb.append("-");
			sb.append(CDSConstants.TRANS_TYPE_CREDIT);
			key = sb.toString();
			if (ehcache.get(key) == null) {
				isValidTrans = true;
				ehcache.put(key, creditPointRequest.getClientTransactionId());

			}
		}else if (creditPointRequest.getClientTransactionId() != null
				&& creditPointRequest.getSessionUUID() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(creditPointRequest.getSessionUUID());
			sb.append("-");
			sb.append(creditPointRequest.getClientTransactionId());
			sb.append("-");
			sb.append(CDSConstants.TRANS_TYPE_CREDIT);
			key = sb.toString();
			if (ehcache.get(key) == null) {
				isValidTrans = true;
				ehcache.put(key, creditPointRequest.getClientTransactionId());

			}
		} else {
			isValidTrans = true;
		}
		if (!isValidTrans) {
			throw new BadRequestException(
					"If the clientTransactionID matches a recent transaction then this error will be thrown.",
					CDSConstants.ACT_MULTIPLE_TRANSACTION);
		}
        //Fetch the member to check it is active
		MemberInfo activeMember=memberDAO.getMemberInfo(creditPointRequest.getMemberId());
		if(activeMember == null){
			throw new BadRequestException(
					ErrorCode.TRANSACTION_NOT_ALLOWED_BY_MEMBER_ERROR,
					ErrorCode.TRANSACTION_NOT_ALLOWED);
		}
		// 2.Fetch point balance, Daily Regular Point,Weekly Regular
		// Point,Yearly Point , Daily Bonus Credit
		// Quantity,Week_Bonus_Credit_Qty from member_account_balance
		AccountBalancePK accountBalancePK = new AccountBalancePK();
		accountBalancePK.setMemberId(creditPointRequest.getMemberId());
		accountBalancePK.setPointAccountType(creditPointRequest
				.getAccountType());
		start = System.nanoTime();
		PointAccountBalance pointBalanceAccount = pointAccountDAO
				.getAccountDetailsWithLock(accountBalancePK);
		 end = System.nanoTime();
         elapsedTime = end - start;
         seconds = (double) elapsedTime / 1000000000.0;
        logger.info("===========Time Taken by getAccountDetailsWithLock ============> "+String.valueOf(seconds));
		PointAccountType pointAccountType = cacheService
				.getAccountType(creditPointRequest.getAccountType());
		// if pointAccountType == null then throw exception
		if(null == pointAccountType){
			logger.error("incorrect account Type "+ creditPointRequest.getAccountType());
			throw new BadRequestException(
					ErrorCode.ACCOUNT_NOT_FOUND_DESC,
					ErrorCode.ACCOUNT_NOT_FOUND);
		} 
		// if pointBalanceAccount != null then update else insert
		if (pointBalanceAccount != null) {
			if (CDSConstants.CREDIT_TYPE_REGULAR.equals(creditPointRequest
					.getCreditType())) {
				if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getDailyCreditQty() > pointAccountType
							.getDailyCreditLimitQty()) {
					logger.info("Daily Regular Credit Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				} else if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getWeekCreditQty() > pointAccountType
							.getWeekCreditLimitQty()) {
					logger.info("Weekly Regular Credit Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				} else if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getYearlyCreditQty() > pointAccountType
							.getYearCreditLimitQty()) {
					logger.info("Yearly Regular Credit Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				} else if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getPointBalance() > pointAccountType
							.getBalanceLimitQty()) {
					logger.info("Point Balance Regular Credit Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				}
				// Update member account balance
				pointBalanceAccount.setCreditOrDebitType(creditPointRequest
						.getCreditType());
				pointBalanceAccount.setPointBalance(pointBalanceAccount
						.getPointBalance()
						+ creditPointRequest.getPointsQuantity());
				pointBalanceAccount.setWeekCreditQty(pointBalanceAccount
						.getWeekCreditQty()
						+ creditPointRequest.getPointsQuantity());
				pointBalanceAccount.setDailyCreditQty(pointBalanceAccount
						.getDailyCreditQty()
						+ creditPointRequest.getPointsQuantity());
				pointBalanceAccount.setYearlyCreditQty(pointBalanceAccount
						.getYearlyCreditQty()
						+ creditPointRequest.getPointsQuantity());
				String statusLevel = getMemberAccountStatusLevel(pointAccountType,pointBalanceAccount.getPointBalance());
				pointBalanceAccount.setStatusLevel(statusLevel);
				start = System.nanoTime();
				pointAccountDAO.updateAccountBalance(pointBalanceAccount);
				 end = System.nanoTime();
	             elapsedTime = end - start;
	             seconds = (double) elapsedTime / 1000000000.0;
	            logger.info("===========Time Taken by updateAccountBalance ============> "+String.valueOf(seconds));

			} else if (CDSConstants.CREDIT_TYPE_BONUS.equals(creditPointRequest
					.getCreditType())) {
				if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getDailyBonusCreditQty() > pointAccountType
							.getDailyBonusLimitQty()) {
					logger.info("Daily Bonus Credit Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				} else if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getWeekBonusCreditQty() > pointAccountType
							.getWeekBonusLimitQty()) {
					logger.info("Weekly Bonus Credit Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				} else if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getYearlyCreditQty() > pointAccountType
							.getYearCreditLimitQty()) {
					logger.info("Yearly Bonus Credit Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				} else if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getPointBalance() > pointAccountType
							.getBalanceLimitQty()) {
					logger.info("Bonus Point Balance Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				}
				// Update member account balance
				pointBalanceAccount.setCreditOrDebitType(creditPointRequest
						.getCreditType());
				pointBalanceAccount.setPointBalance(pointBalanceAccount
						.getPointBalance()
						+ creditPointRequest.getPointsQuantity());
				pointBalanceAccount.setDailyBonusCreditQty(pointBalanceAccount
						.getDailyBonusCreditQty()
						+ creditPointRequest.getPointsQuantity());
				pointBalanceAccount.setWeekBonusCreditQty(pointBalanceAccount
						.getWeekBonusCreditQty()
						+ creditPointRequest.getPointsQuantity());
				pointBalanceAccount.setYearlyCreditQty(pointBalanceAccount
						.getYearlyCreditQty()
						+ creditPointRequest.getPointsQuantity());
				start = System.nanoTime();
				String statusLevel = getMemberAccountStatusLevel(pointAccountType,pointBalanceAccount.getPointBalance());
				pointBalanceAccount.setStatusLevel(statusLevel);
				pointAccountDAO.updateAccountBalance(pointBalanceAccount);
				 end = System.nanoTime();
	             elapsedTime = end - start;
	             seconds = (double) elapsedTime / 1000000000.0;
	            logger.info("===========Time Taken by updateAccountBalance ============> "+String.valueOf(seconds));

			} else if (CDSConstants.CREDIT_TYPE_ADMIN.equals(creditPointRequest
					.getCreditType())) {
				if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getYearlyCreditQty() > pointAccountType
							.getYearCreditLimitQty()) {
					logger.info("Admin yearly Point Limit Qty is Hit");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				} else if (creditPointRequest.getPointsQuantity()
						+ pointBalanceAccount.getPointBalance() > pointAccountType
							.getBalanceLimitQty()) {
					logger.info("Point Balance Limit Qty is Hit for Admin");
					throw new BadRequestException(
							ErrorCode.ACT_MAX_CREDIT_HIT_DESC,
							ErrorCode.ACT_MAX_CREDIT_HIT);
				}
				// Update member account balance
				pointBalanceAccount.setCreditOrDebitType(creditPointRequest
						.getCreditType());
				pointBalanceAccount.setPointBalance(pointBalanceAccount
						.getPointBalance()
						+ creditPointRequest.getPointsQuantity());
				pointBalanceAccount.setYearlyCreditQty(pointBalanceAccount
						.getYearlyCreditQty()
						+ creditPointRequest.getPointsQuantity());
				start = System.nanoTime();
				String statusLevel = getMemberAccountStatusLevel(pointAccountType,pointBalanceAccount.getPointBalance());
				pointBalanceAccount.setStatusLevel(statusLevel);
				pointAccountDAO.updateAccountBalance(pointBalanceAccount);
				 end = System.nanoTime();
	             elapsedTime = end - start;
	             seconds = (double) elapsedTime / 1000000000.0;
	            logger.info("===========Time Taken by updateAccountBalance ============> "+String.valueOf(seconds));
			}

		} else {
			//insert the data as this is a fresh/New request
			if(CDSConstants.CREDIT_TYPE_REGULAR.equals(creditPointRequest.getCreditType())){
			if(creditPointRequest.getPointsQuantity() > pointAccountType.getDailyCreditLimitQty()){
				logger.info("Daily Regular Credit Limit Qty is Hit");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}else if(creditPointRequest.getPointsQuantity() >pointAccountType.getWeekCreditLimitQty()){
				logger.info("Weekly Regular Credit Limit Qty is Hit");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}else if(creditPointRequest.getPointsQuantity() > pointAccountType.getYearCreditLimitQty()){
				logger.info("Yearly Regular Credit Limit Qty is Hit");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}else if(creditPointRequest.getPointsQuantity() > pointAccountType.getBalanceLimitQty()){
				logger.info("Point Balance Regular Credit Limit Qty is Hit");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}
			// Insert member account balance
			pointBalanceAccount = new PointAccountBalance();
			pointBalanceAccount.setPointAccountType(creditPointRequest.getAccountType());
			pointBalanceAccount.setMemberId(creditPointRequest.getMemberId());
			pointBalanceAccount.setPointBalance(creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setWeekCreditQty(creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setDailyCreditQty(creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setYearlyCreditQty(creditPointRequest.getPointsQuantity());
			String statusLevel = getMemberAccountStatusLevel(pointAccountType,creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setStatusLevel(statusLevel);
			pointAccountDAO.insertAccountBalance(pointBalanceAccount);
							
		}else if(CDSConstants.CREDIT_TYPE_BONUS.equals(creditPointRequest.getCreditType())){
			if(creditPointRequest.getPointsQuantity() > pointAccountType.getDailyBonusLimitQty()){
				logger.info("Daily Bonus Credit Limit Qty is Hit");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}else if(creditPointRequest.getPointsQuantity() > pointAccountType.getWeekBonusLimitQty()){
				logger.info("Weekly Bonus Credit Limit Qty is Hit");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}else if(creditPointRequest.getPointsQuantity() > pointAccountType.getYearCreditLimitQty()){
				logger.info("Yearly Bonus Credit Limit Qty is Hit");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}else if(creditPointRequest.getPointsQuantity() > pointAccountType.getBalanceLimitQty()){
				logger.info("Point Balance Bonus Credit Limit Qty is Hit");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}
			// Insert member account balance
			pointBalanceAccount = new PointAccountBalance();
			pointBalanceAccount.setPointAccountType(creditPointRequest.getAccountType());
			pointBalanceAccount.setMemberId(creditPointRequest.getMemberId());
			pointBalanceAccount.setPointBalance(creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setDailyBonusCreditQty(creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setWeekBonusCreditQty(creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setYearlyCreditQty(creditPointRequest.getPointsQuantity());
			String statusLevel = getMemberAccountStatusLevel(pointAccountType,creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setStatusLevel(statusLevel);
			pointAccountDAO.insertAccountBalance(pointBalanceAccount);
			
		}else if(CDSConstants.CREDIT_TYPE_ADMIN.equals(creditPointRequest.getCreditType())){
			if(creditPointRequest.getPointsQuantity() > pointAccountType.getYearCreditLimitQty()){
				logger.info("Yearly Credit Limit Qty is Hit for Admin");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}else if(creditPointRequest.getPointsQuantity() > pointAccountType.getBalanceLimitQty()){
				logger.info("Point Balance Credit Limit Qty is Hit for Admin");
				throw new BadRequestException(ErrorCode.ACT_MAX_CREDIT_HIT_DESC,ErrorCode.ACT_MAX_CREDIT_HIT);
			}
			// Insert member account balance
			pointBalanceAccount = new PointAccountBalance();
			pointBalanceAccount.setPointAccountType(creditPointRequest.getAccountType());
			pointBalanceAccount.setMemberId(creditPointRequest.getMemberId());
			pointBalanceAccount.setPointBalance(pointBalanceAccount.getPointBalance()+creditPointRequest.getPointsQuantity());
			pointBalanceAccount.setYearlyCreditQty(pointBalanceAccount.getYearlyCreditQty()+creditPointRequest.getPointsQuantity());
			String statusLevel = getMemberAccountStatusLevel(pointAccountType,pointBalanceAccount.getPointBalance());
			pointBalanceAccount.setStatusLevel(statusLevel);
			pointAccountDAO.insertAccountBalance(pointBalanceAccount);
		}

	}
		// populate transaction history
		TransactionHistory history = populateTransactionHistory(creditPointRequest);
		SequenceNumber sequenceNumber = new SequenceNumber(
				CDSConstants.TRANS_ID_SEQ);
		start = System.nanoTime();
		BigInteger transactionId = sequenceNumberDAO
				.getNextSequenceNumber(sequenceNumber);
		end = System.nanoTime();
        elapsedTime = end - start;
        seconds = (double) elapsedTime / 1000000000.0;
       logger.info("===========Time Taken by getNextSequenceNumber ============> "+String.valueOf(seconds));
		history.setPointTransId(transactionId);
		history.setSessionId(sessionId);
		// set TransDT time stamp
		// history.setTransactionDate(transactionDate);
		history.setTransactionTimeStampString(transactionTimeStampString);
		history.setBalance(new BigInteger(String.valueOf(pointBalanceAccount.getPointBalance())));
		history.setStatusLevel(pointBalanceAccount.getStatusLevel());
		start = System.nanoTime();
		pointAccountDAO.insertTransactionHistory(history);
		 end = System.nanoTime();
         elapsedTime = end - start;
         seconds = (double) elapsedTime / 1000000000.0;
        logger.info("===========Time Taken by insertTransactionHistory ============> "+String.valueOf(seconds));

		return transactionId;
	}
	
	public String getMemberAccountStatusLevel (PointAccountType pointAccountType , int pointBalance){
		String statusLevel = "";
		if((pointBalance < Integer.parseInt(pointAccountType.getLevel_1_limit())) ||(pointAccountType.getLevel_1_limit() == null)){
			statusLevel = null;
			
		}else if( (pointBalance >= Integer.parseInt(pointAccountType.getLevel_1_limit()) && pointBalance < Integer.parseInt(pointAccountType.getLevel_2_limit())) || (pointAccountType.getLevel_2_limit() == null)){
			statusLevel = pointAccountType.getLevel_1_name();
			
		}else if ((pointBalance >= Integer.parseInt(pointAccountType.getLevel_2_limit()) &&  pointBalance < Integer.parseInt(pointAccountType.getLevel_3_limit())) ||(pointAccountType.getLevel_3_limit() == null)){
			statusLevel = pointAccountType.getLevel_2_name();
			
		}else if ((pointBalance >= Integer.parseInt(pointAccountType.getLevel_3_limit()) &&  pointBalance < Integer.parseInt(pointAccountType.getLevel_4_limit())) ||(pointAccountType.getLevel_4_limit() == null)){
			statusLevel = pointAccountType.getLevel_3_name();
			
		}else if ((pointBalance >= Integer.parseInt(pointAccountType.getLevel_4_limit()) &&  pointBalance < Integer.parseInt(pointAccountType.getLevel_5_limit()))||(pointAccountType.getLevel_5_limit() == null)){
			statusLevel = pointAccountType.getLevel_4_name();
		}else if(pointBalance >= Integer.parseInt(pointAccountType.getLevel_5_limit())){
			statusLevel = pointAccountType.getLevel_5_name();
		}
		return statusLevel;
	}
	public BigInteger debitPoint(DebitPointRequest debitPointRequest)
			throws BadRequestException {
		// capture transDT
		// Date transactionDate = CDSOUtils.getCurrentUTCDate();
		String transactionTimeStampString = CDSOUtils.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime());
		String validationErrors = null;
		BigInteger transactionId = null;
		try {
			validationErrors = CDSOUtils.validate(debitPointRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("creditPoint :", e);
			throw new WebApplicationException(e);
		}
		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);

		}

		// 1. get session_id from site_session table using sessionUUID using cache
		/*BigInteger sessionId = sessionIdDAO.getSessionID(debitPointRequest
				.getSessionUUID());*/
		BigInteger sessionId = null;
		if(debitPointRequest
				.getSessionUUID() != null){
			sessionId = cacheService.getSessionID(debitPointRequest
					.getSessionUUID());
		}
		// 2. Check for the sessionId and clientTransactionId in cache
		Cache ehcache = cacheManager
				.getCache(CDSConstants.CLIENT_TRANS_ID_CACHE);
		// if sessionUUID and clientTransId is not null
		String key = null;
		boolean isValidTrans = false;
		PointAccountType pointAccountType = cacheService
				.getAccountType(debitPointRequest.getAccountType());
		// if pointAccountType == null then throw exception
		if(null == pointAccountType){
			logger.error("incorrect account Type "+ debitPointRequest.getAccountType());
			throw new BadRequestException(
					ErrorCode.ACCOUNT_NOT_FOUND_DESC,
					ErrorCode.ACCOUNT_NOT_FOUND);
		} 
		// ClientTransactionId should be shared across type
		if (debitPointRequest.getClientTransactionId() != null
				&& debitPointRequest.getSessionUUID() == null) {
			StringBuffer sb = new StringBuffer();
			sb.append(debitPointRequest.getClientTransactionId());
			sb.append("-");
			sb.append(CDSConstants.TRANS_TYPE_DEBIT);
			key = sb.toString();
			if (ehcache.get(key) == null) {
				isValidTrans = true;
				ehcache.put(key, debitPointRequest.getClientTransactionId());

			}
		}else if (debitPointRequest.getClientTransactionId() != null
				&& debitPointRequest.getSessionUUID() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(debitPointRequest.getSessionUUID());
			sb.append("-");
			sb.append(debitPointRequest.getClientTransactionId());
			sb.append("-");
			sb.append(CDSConstants.TRANS_TYPE_DEBIT);
			key = sb.toString();
			if (ehcache.get(key) == null) {
				isValidTrans = true;
				ehcache.put(key, debitPointRequest.getClientTransactionId());

			}
		} else {
			isValidTrans = true;
		}
		if (!isValidTrans) {
			throw new BadRequestException(
					"If the clientTransactionID matches a recent transaction then this error will be thrown.",
					CDSConstants.ACT_MULTIPLE_TRANSACTION);
		}
		 //Fetch the member to check it is active
		MemberInfo activeMember=memberDAO.getMemberInfo(debitPointRequest.getMemberId());
		if(activeMember == null){
			throw new BadRequestException(
					ErrorCode.TRANSACTION_NOT_ALLOWED_BY_MEMBER_ERROR,
					ErrorCode.TRANSACTION_NOT_ALLOWED);
		}
		// 2.Fetch point balance, Daily Regular Point,Weekly Regular
		// Point,Yearly Point , Daily Bonus Credit
		// Quantity,Week_Bonus_Credit_Qty from member_account_balance
		AccountBalancePK accountBalancePK = new AccountBalancePK();
		accountBalancePK.setMemberId(debitPointRequest.getMemberId());
		accountBalancePK
				.setPointAccountType(debitPointRequest.getAccountType());
		long start = System.nanoTime();
		PointAccountBalance pointBalanceAccount = pointAccountDAO
				.getAccountDetailsWithLock(accountBalancePK);
		long end = System.nanoTime();
        long elapsedTime = end - start;
        double seconds = (double) elapsedTime / 1000000000.0;
        logger.info("===========Time Taken by getAccountDetailsWithLock ============> "+String.valueOf(seconds));
        // If debit amount is 0, debit type reqular and no record found in member_account_balance_table
        //handle it seperately
        if(pointBalanceAccount == null && CDSConstants.DEBIT_TYPE_REGULAR.equals(debitPointRequest
				.getDebitType()) &&  debitPointRequest.getPointsQuantity()==0){
        			// Insert member account balance
        				pointBalanceAccount = new PointAccountBalance();
        				pointBalanceAccount.setPointAccountType(debitPointRequest.getAccountType());
        				pointBalanceAccount.setMemberId(debitPointRequest.getMemberId());
        				pointBalanceAccount.setPointBalance(0);
        				pointBalanceAccount.setWeekCreditQty(0);
        				pointBalanceAccount.setDailyCreditQty(0);
        				pointBalanceAccount.setYearlyCreditQty(0);
        				String statusLevel = getMemberAccountStatusLevel(pointAccountType,0);
        				pointBalanceAccount.setStatusLevel(statusLevel);
        				pointAccountDAO.insertAccountBalance(pointBalanceAccount);
        				// populate transaction history
        				TransactionHistory history = populateTransactionHistory(debitPointRequest);
        				SequenceNumber sequenceNumber = new SequenceNumber(
        						CDSConstants.TRANS_ID_SEQ);
        				start = System.nanoTime();
        				transactionId = sequenceNumberDAO
        						.getNextSequenceNumber(sequenceNumber);
        				history.setPointTransId(transactionId);
        				history.setSessionId(sessionId);
        				history.setHoldTime(null);
        				// set TransDT time stamp
        				// history.setTransactionDate(transactionDate);
        				history.setTransactionTimeStampString(transactionTimeStampString);
        				history.setBalance(new BigInteger("0"));
        				pointAccountDAO.insertTransactionHistory(history);
        				return transactionId;
        }
		if (pointBalanceAccount == null) {
			throw new BadRequestException(
					"Account details not found for memberId "
							+ accountBalancePK.getMemberId()
							+ " and Account Type "
							+ accountBalancePK.getPointAccountType(),
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		if (pointBalanceAccount.getPointBalance() < debitPointRequest
				.getPointsQuantity()
				&& !CDSConstants.DEBIT_TYPE_CONFIRM.equals(debitPointRequest
						.getDebitType())) {
			throw new BadRequestException(
					ErrorCode.ACT_INSUFFICIENT_POINTS_DESC,
					ErrorCode.ACT_INSUFFICIENT_POINTS);
		}
		// if debitType is regular or hold, update point balance

		if (CDSConstants.DEBIT_TYPE_REGULAR.equals(debitPointRequest
				.getDebitType())) {
			pointBalanceAccount.setPointBalance(pointBalanceAccount
					.getPointBalance() - debitPointRequest.getPointsQuantity());
			String statusLevel = getMemberAccountStatusLevel(pointAccountType,pointBalanceAccount
					.getPointBalance());
			pointBalanceAccount.setStatusLevel(statusLevel);
			start = System.nanoTime();
			pointAccountDAO.updateAccountBalance(pointBalanceAccount);
			 end = System.nanoTime();
             elapsedTime = end - start;
             seconds = (double) elapsedTime / 1000000000.0;
            logger.info("===========Time Taken by updateAccountBalance ============> "+String.valueOf(seconds));
			// populate transaction history
			TransactionHistory history = populateTransactionHistory(debitPointRequest);
			SequenceNumber sequenceNumber = new SequenceNumber(
					CDSConstants.TRANS_ID_SEQ);
			start = System.nanoTime();
			transactionId = sequenceNumberDAO
					.getNextSequenceNumber(sequenceNumber);
			history.setPointTransId(transactionId);
			history.setSessionId(sessionId);
			history.setHoldTime(null);
			// set TransDT time stamp
			// history.setTransactionDate(transactionDate);
			history.setTransactionTimeStampString(transactionTimeStampString);
			history.setBalance(new BigInteger(String.valueOf(pointBalanceAccount.getPointBalance())));
			history.setStatusLevel(statusLevel);
			start = System.nanoTime();
			pointAccountDAO.insertTransactionHistory(history);
			 end = System.nanoTime();
             elapsedTime = end - start;
             seconds = (double) elapsedTime / 1000000000.0;
            logger.info("===========Time Taken by insertTransactionHistory ============> "+String.valueOf(seconds));
		}
		if (CDSConstants.DEBIT_TYPE_HOLD.equals(debitPointRequest
				.getDebitType())) {
			pointBalanceAccount.setPointBalance(pointBalanceAccount
					.getPointBalance() - debitPointRequest.getPointsQuantity());
			String statusLevel = getMemberAccountStatusLevel(pointAccountType,pointBalanceAccount
					.getPointBalance());
			pointBalanceAccount.setStatusLevel(statusLevel);
			pointAccountDAO.updateAccountBalance(pointBalanceAccount);
			// populate transaction history
			TransactionHistory history = populateTransactionHistory(debitPointRequest);
			SequenceNumber sequenceNumber = new SequenceNumber(
					CDSConstants.TRANS_ID_SEQ);
			transactionId = sequenceNumberDAO
					.getNextSequenceNumber(sequenceNumber);
			history.setPointTransId(transactionId);
			history.setSessionId(sessionId);
			if(debitPointRequest.getHoldTime() == null)
				history.setHoldTime(CDSConstants.DEFAULT_HOLD_TIME);
			else
			  history.setHoldTime(debitPointRequest.getHoldTime());
			// set TransDT time stamp
			// history.setTransactionDate(transactionDate);
			history.setTransactionTimeStampString(transactionTimeStampString);
			history.setBalance(new BigInteger(String.valueOf(pointBalanceAccount.getPointBalance())));
			history.setStatusLevel(statusLevel);
			start = System.nanoTime();
			pointAccountDAO.insertTransactionHistory(history);
			 end = System.nanoTime();
             elapsedTime = end - start;
             seconds = (double) elapsedTime / 1000000000.0;
            logger.info("===========Time Taken by insertTransactionHistory ============> "+String.valueOf(seconds));
		}
		if (CDSConstants.DEBIT_TYPE_CONFIRM.equals(debitPointRequest
				.getDebitType())) {
			logger.info("For Debit Type confirm for member"
					+ debitPointRequest.getMemberId() + " AccountType "
					+ debitPointRequest.getAccountType()
					+ " relatedTransactionId is "
					+ debitPointRequest.getRelatedTransactionId());
			if (debitPointRequest.getRelatedTransactionId() == null) {
				logger.info("For Debit Type confirm for member"
						+ debitPointRequest.getMemberId() + " AccountType "
						+ debitPointRequest.getAccountType()
						+ " relatedTransactionId is null");
				throw new BadRequestException(
						ErrorCode.ACT_HOLDCONFIRMATION_FAILED_DESC
								+ " relatedTransactionId "
								+ debitPointRequest.getRelatedTransactionId(),
						ErrorCode.ACT_HOLDCONFIRMATION_FAILED);
			}
			// Fetch eariler transaction using related transactionId for update
			start =System.nanoTime();
			TransactionHistory oldTransHistory = pointAccountDAO
					.getPointHistoryByTransactionIdWithLock(debitPointRequest
							.getRelatedTransactionId());
			end = System.nanoTime();
            elapsedTime = end - start;
            seconds = (double) elapsedTime / 1000000000.0;
           logger.info("===========Time Taken by getPointHistoryByTransactionIdWithLock ============> "+String.valueOf(seconds));
			if (oldTransHistory != null) {
				if(oldTransHistory.getPointsType().equalsIgnoreCase(CDSConstants.DEBIT_TYPE_ROLLBACK)){
					logger.info("For Debit Type confirm for member"
							+ debitPointRequest.getMemberId() + " AccountType "
							+ debitPointRequest.getAccountType()
							+ " relatedTransactionId "+ oldTransHistory.getRelatedTransactionId() +" Point Type is " +oldTransHistory.getPointsType());
					throw new BadRequestException(
							ErrorCode.ACT_HOLDCONFIRMATION_FAILED_DESC
									+ " relatedTransactionId "
									+ debitPointRequest.getRelatedTransactionId(),
							ErrorCode.ACT_HOLDCONFIRMATION_FAILED);
				}
				oldTransHistory.setPointsType(debitPointRequest.getDebitType());
				oldTransHistory.setHoldTime(null);
				// oldTransHistory.setRelatedTransactionId(debitPointRequest.getRelatedTransactionID());
				oldTransHistory.setSessionId(sessionId);
				oldTransHistory.setClientTransId(debitPointRequest
						.getClientTransactionId());
				// set TransDT time stamp
				// history.setTransactionDate(transactionDate);
				oldTransHistory
						.setTransactionTimeStampString(transactionTimeStampString);
				start = System.nanoTime();
				pointAccountDAO.updatePointHistory(oldTransHistory);
				transactionId = oldTransHistory.getPointTransId();
				 end = System.nanoTime();
	             elapsedTime = end - start;
	             seconds = (double) elapsedTime / 1000000000.0;
	            logger.info("===========Time Taken by updatePointHistory ============> "+String.valueOf(seconds));
			} else {

				throw new BadRequestException(
						ErrorCode.ACT_HOLDCONFIRMATION_FAILED_DESC
								+ " relatedTransactionId "
								+ debitPointRequest.getRelatedTransactionId(),
						ErrorCode.ACT_HOLDCONFIRMATION_FAILED);
			}
		}
		return transactionId;

	}

	private TransactionHistory populateTransactionHistory(
			CreditPointRequest creditPointRequest) {
		TransactionHistory history = new TransactionHistory();
		history.setAccountType(creditPointRequest.getAccountType());
		history.setRelatedTransactionId(creditPointRequest
				.getRelatedTransactionId());
		history.setClientTransId(creditPointRequest.getClientTransactionId());
		history.setMemberId(creditPointRequest.getMemberId());
		history.setPointsQuanity(creditPointRequest.getPointsQuantity());
		history.setPointsSource(creditPointRequest.getPointsSource());
		history.setReasonCode(creditPointRequest.getReasonCode());
		history.setTransactionType(CDSConstants.TRANS_TYPE_CREDIT);
		history.setPointsType(creditPointRequest.getCreditType());
		history.setPromotionId(creditPointRequest.getPromotion());
		history.setStakeHolderId(creditPointRequest.getStakeHolder());
		history.setProductID(creditPointRequest.getProductId());
		history.setBrandId(creditPointRequest.getBrandId());
		return history;
	}

	private TransactionHistory populateTransactionHistory(
			DebitPointRequest debitPointRequest) {
		TransactionHistory history = new TransactionHistory();
		history.setAccountType(debitPointRequest.getAccountType());
		history.setRelatedTransactionId(debitPointRequest
				.getRelatedTransactionId());
		history.setClientTransId(debitPointRequest.getClientTransactionId());
		history.setMemberId(debitPointRequest.getMemberId());
		history.setPointsQuanity(debitPointRequest.getPointsQuantity());
		history.setPointsSource(debitPointRequest.getPointsSource());
		history.setReasonCode(debitPointRequest.getReasonCode());
		history.setTransactionType(CDSConstants.TRANS_TYPE_DEBIT);
		history.setPointsType(debitPointRequest.getDebitType());
		history.setPromotionId(debitPointRequest.getPromotion());
		history.setStakeHolderId(debitPointRequest.getStakeHolder());
		history.setProductID(debitPointRequest.getProductId());
		history.setHoldTime(debitPointRequest.getHoldTime());
		history.setItemId(debitPointRequest.getItemId());
		history.setBrandId(debitPointRequest.getBrandId());
		return history;
	}

	public PointAccountDAO getPointAccountDAO() {
		return pointAccountDAO;
	}

	public void setPointAccountDAO(PointAccountDAO pointAccountDAO) {
		this.pointAccountDAO = pointAccountDAO;
	}

	public SequenceNumberDAO getSequenceNumberDAO() {
		return sequenceNumberDAO;
	}

	public void setSequenceNumberDAO(SequenceNumberDAO sequenceNumberDAO) {
		this.sequenceNumberDAO = sequenceNumberDAO;
	}

	public EhCacheCacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(EhCacheCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public PointAccountBalance getToMemberObject(
			List<PointAccountBalance> listPointAccountBalance,
			PointAccountBalance pointAccountBalance) {
		for (PointAccountBalance pointAccountBalance1 : listPointAccountBalance) {
			if (pointAccountBalance1.getPointAccountType().equals(
					pointAccountBalance.getPointAccountType())) {
				return pointAccountBalance1;

			}
		}

		return null;
	}

	// updatePointBalnace for mergeMember API
	public void updatePointBalanceForMerge(BigInteger fromMemberId, BigInteger toMemberId) throws BadRequestException {
		//get the pointbalance details of From ID 
		List<PointAccountBalance> fromPointAccountBalance = pointAccountDAO.getAccountDetailsByMemberId(fromMemberId);
		//get the point balance details of To memebr ID
		List<PointAccountBalance> toPointAccountBalance = pointAccountDAO.getAccountDetailsByMemberIdWithLock(toMemberId);
		if(toPointAccountBalance != null && toPointAccountBalance.size()>0){
			for (PointAccountBalance fromPointBalance : fromPointAccountBalance) {
				PointAccountType pointAccount = new PointAccountType();
				PointAccountBalance toPointBalance = new PointAccountBalance();
				Boolean flag = false;
				for (PointAccountBalance toPointBalanceTemp : toPointAccountBalance) {
					//there is no concept for credit or debit type in case of Merge
					toPointBalance = toPointBalanceTemp;
					if(fromPointBalance.getPointAccountType().equalsIgnoreCase(toPointBalance.getPointAccountType())){
						flag = true;
						break;
					}
				}
				if(flag){
					//get the point details of To account
					List<Integer> minValueToAdd = new ArrayList<Integer>(3);
					pointAccount = cacheService.getAccountType(toPointBalance.getPointAccountType());
					//get the point Balance Data of To and FROM Account Daily
					if (fromPointBalance.getPointBalance()+toPointBalance.getDailyCreditQty()>pointAccount.getDailyCreditLimitQty()){
						minValueToAdd.add(pointAccount.getDailyCreditLimitQty() - toPointBalance.getDailyCreditQty());
					}else{
						minValueToAdd.add(fromPointBalance.getPointBalance());
					}
					//get the point Balance Data of To and FROM Account Weekly
					if (fromPointBalance.getPointBalance()+toPointBalance.getWeekCreditQty()>pointAccount.getWeekCreditLimitQty()){
						minValueToAdd.add(pointAccount.getWeekCreditLimitQty() - toPointBalance.getWeekCreditQty());
					}else{
						minValueToAdd.add(fromPointBalance.getPointBalance());
					}
					//get the point Balance Data of To and FROM Account Yearly
					if (fromPointBalance.getPointBalance()+toPointBalance.getYearlyCreditQty()>pointAccount.getYearCreditLimitQty()){
						minValueToAdd.add(pointAccount.getYearCreditLimitQty()- toPointBalance.getYearlyCreditQty());
					}else{
						minValueToAdd.add(fromPointBalance.getPointBalance());
					}
					
					//which ever is lower of yearly,weekly,daily we have to add that amount in all Qty
					int minPointBalance = minValueToAdd.get(0);
					int i = 0;
					/*if(minValueToAdd.get(0) ==0){
						minPointBalance = minValueToAdd.get(1);
						i=1;
					}else if(minValueToAdd.get(1) == 0){
						minPointBalance = minValueToAdd.get(2);
						i=2;
					}*/
					
				    for (int j = i; j < minValueToAdd.size(); j++) {
				    	 if(minValueToAdd.get(j) < minPointBalance){
					            minPointBalance = minValueToAdd.get(j);
					        }
					}
				    //Checking this minimum point balance value for the Balance Limit Qty for that account after obtaining the min value from all the limits
				    if(minPointBalance+toPointBalance.getPointBalance()> pointAccount.getBalanceLimitQty()){
				    	minPointBalance = pointAccount.getBalanceLimitQty() - toPointBalance.getPointBalance();
				    }
				    logger.info("The lowest value is "+minPointBalance+" that will be ADDED during the Regular merge to the Member "+ toMemberId);
				    /*if(minValueToAdd.get(0) ==0){
				    	//add only for Week and Year
				    	logger.trace("adding credit point Qty for Week and Year from Member "+ fromMemberId);
				    	toPointBalance.setDailyCreditQty(toPointBalance.getDailyCreditQty());
					    toPointBalance.setWeekCreditQty(minPointBalance+toPointBalance.getWeekCreditQty());
					    toPointBalance.setYearlyCreditQty(minPointBalance+toPointBalance.getYearlyCreditQty());
				    }else if (minValueToAdd.get(1) ==0){
				    	//add only for year
				    	logger.trace("adding for year only from Member "+ fromMemberId);
				    	toPointBalance.setDailyCreditQty(toPointBalance.getDailyCreditQty());
					    toPointBalance.setWeekCreditQty(toPointBalance.getWeekCreditQty());
					    toPointBalance.setYearlyCreditQty(minPointBalance+toPointBalance.getYearlyCreditQty());
				    }else{*/
				    	//add for all
				    	logger.trace("adding Credit Points Qty for all from Member "+ fromMemberId);
					    toPointBalance.setDailyCreditQty(minPointBalance+toPointBalance.getDailyCreditQty());
					    toPointBalance.setWeekCreditQty(minPointBalance+toPointBalance.getWeekCreditQty());
					    toPointBalance.setYearlyCreditQty(minPointBalance+toPointBalance.getYearlyCreditQty());
				   // }
				    toPointBalance.setPointBalance(minPointBalance+toPointBalance.getPointBalance());

				    //setting the last activity date based on the one that is recent
				    if(fromPointBalance.getLastActivityDt() != null){
				    	if(toPointBalance.getLastActivityDt() == null || toPointBalance.getLastActivityDt().before(fromPointBalance.getLastActivityDt())){
					    	toPointBalance.setLastActivityDt(fromPointBalance.getLastActivityDt());
					    }
				    }
				    //toPointBalance.setInsertDt(fromPointBalance.getInsertDt());
				  if(minPointBalance>0){
					//Set the status level
				  String statusLevel = getMemberAccountStatusLevel(pointAccount,toPointBalance.getPointBalance());
				  toPointBalance.setStatusLevel(statusLevel);
				  //update the To member 
				    pointAccountDAO.updateAccountBalanceForMerge(toPointBalance);
				  //Recording the Merge Transaction in points transaction Table for the Trace.
				    logPointsMergedInPointHistory(minPointBalance,fromPointBalance.getPointAccountType(),toMemberId,toPointBalance);
				  }else{
					  logger.info("Minimun Value to add is 0 Hence Points are not logged into PointTrans and Member Account data Remains Unaffected");
				  }
				    minValueToAdd.clear();
				}else{
					logger.info("Merging the Lite Account To FUll Account But the Full Account Has no Such Matching Account Type "+fromPointBalance.getPointAccountType() + "Lite Member ID =>  "+fromPointBalance.getMemberId());
					mergeLitToFullWhereFullIsNull(fromPointBalance,toMemberId);
				}
			}
		}else{
			//add data freshly.
			for (PointAccountBalance fromPointBalance : fromPointAccountBalance) {
				mergeLitToFullWhereFullIsNull(fromPointBalance,toMemberId);
			}
		}
	}

	public void mergeLitToFullWhereFullIsNull(PointAccountBalance fromPointBalance,BigInteger toMemberId){
			logger.info("Its a new Addition of points for the Account "+ fromPointBalance.getPointAccountType());
			PointAccountType pointAccount = new PointAccountType();
			PointAccountBalance toPointBalance = new PointAccountBalance();
				
			//get the point details of from Account Type and match it with the available account Types in DB and insert AS with limit check
			pointAccount = cacheService.getAccountType(fromPointBalance.getPointAccountType());
			if(pointAccount != null){
				
				//get the point details of To account
				List<Integer> minValueToAdd = new ArrayList<Integer>(3);
				//get the point Balance Data of To and FROM Account Daily
				if (fromPointBalance.getPointBalance()>pointAccount.getDailyCreditLimitQty()){
					minValueToAdd.add(pointAccount.getDailyCreditLimitQty());
				}else{
					minValueToAdd.add(fromPointBalance.getPointBalance());
				}
				//get the point Balance Data of To and FROM Account Weekly
				if (fromPointBalance.getPointBalance()>pointAccount.getWeekCreditLimitQty()){
					minValueToAdd.add(pointAccount.getWeekCreditLimitQty());
				}else{
					minValueToAdd.add(fromPointBalance.getPointBalance());
				}
				//get the point Balance Data of To and FROM Account Yearly
				if (fromPointBalance.getPointBalance()>pointAccount.getYearCreditLimitQty()){
					minValueToAdd.add(pointAccount.getYearCreditLimitQty());
				}else{
					minValueToAdd.add(fromPointBalance.getPointBalance());
				}
				//which ever is lower of yearly,weekly,daily we have to add that amount in all Qty
				int minPointBalance = minValueToAdd.get(0);
			
			    for (int j = 0; j < minValueToAdd.size(); j++) {
			    	 if(minValueToAdd.get(j) < minPointBalance){
				            minPointBalance = minValueToAdd.get(j);
				        }
				}
			    //Checking this minimum point balance value for the Balance Limit Qty for that account after obtaining the min value from all the limits
			    if(minPointBalance+toPointBalance.getPointBalance()> pointAccount.getBalanceLimitQty()){
			    	minPointBalance = pointAccount.getBalanceLimitQty() - toPointBalance.getPointBalance();
			    }
			    logger.info("The lowest value is "+minPointBalance+" that will be ADDED during the merge to the Member "+ toMemberId);
			    	//add for all
			    logger.trace("adding Credit Points Qty for all from Member "+ fromPointBalance.getMemberId());
				toPointBalance.setDailyCreditQty(minPointBalance+toPointBalance.getDailyCreditQty());
				toPointBalance.setWeekCreditQty(minPointBalance+toPointBalance.getWeekCreditQty());
				toPointBalance.setYearlyCreditQty(minPointBalance+toPointBalance.getYearlyCreditQty());
						
			   // toPointBalance.setPointBalance(fromPointBalance.getPointBalance());
				toPointBalance.setPointBalance(minPointBalance+toPointBalance.getPointBalance());				
			    toPointBalance.setMemberId(toMemberId);
			    toPointBalance.setPointAccountType(fromPointBalance.getPointAccountType());
			    toPointBalance.setLastActivityDt(fromPointBalance.getLastActivityDt());
			    toPointBalance.setInsertDt(fromPointBalance.getInsertDt());
			    //update the To member 
			    if(minPointBalance>0){
			    	 String statusLevel = getMemberAccountStatusLevel(pointAccount,toPointBalance.getPointBalance());
					  toPointBalance.setStatusLevel(statusLevel);
				    pointAccountDAO.insertAccountBalanceForMerge(toPointBalance);
				  //Recording the Merge Transaction in points transaction Table for the Trace.
				    logPointsMergedInPointHistory(fromPointBalance.getPointBalance(),fromPointBalance.getPointAccountType(),toMemberId,toPointBalance);
				 }else{
					  logger.info("Minimun Value to add is 0 Hence Points are not logged into PointTrans and Member Account data Remains Unaffected");
				  }
			}
			
	}
	
	
	public void logPointsMergedInPointHistory(Integer pointsTransfered, String pointAccountType,BigInteger toMemberId,PointAccountBalance toPointBalance){
		TransactionHistory memberMergePtsHistory=new TransactionHistory();
	    memberMergePtsHistory.setAccountType(pointAccountType);
	    memberMergePtsHistory.setMemberId(toMemberId);
	    memberMergePtsHistory.setPointsQuanity(pointsTransfered);
	    memberMergePtsHistory.setPointsSource(CDSConstants.MERGE_SOURCE_CODE);
	    memberMergePtsHistory.setPointsType(CDSConstants.CREDIT_TYPE_ADMIN);
	    memberMergePtsHistory.setReasonCode(CDSConstants.MERGE_REASON_CODE);
	    memberMergePtsHistory.setTransactionTimeStampString(CDSOUtils.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
	    memberMergePtsHistory.setTransactionType(CDSConstants.MERGE_TYPE_CODE);
	    SequenceNumber sequenceNumber = new SequenceNumber(CDSConstants.TRANS_ID_SEQ);
	    memberMergePtsHistory.setPointTransId(sequenceNumberDAO.getNextSequenceNumber(sequenceNumber));
	    //code to update the balance column for the insert
	    memberMergePtsHistory.setBalance(new BigInteger(String.valueOf(toPointBalance.getPointBalance())));
	    memberMergePtsHistory.setStatusLevel(toPointBalance.getStatusLevel());
        pointAccountDAO.insertTransactionHistory(memberMergePtsHistory);
	}

	
	// updatePointBalnace for mergeMember API
		public void updatePointBalanceForBonusMerge(BigInteger fromMemberId, BigInteger toMemberId) throws BadRequestException {
			//get the pointbalance details of From ID 
			List<PointAccountBalance> fromPointAccountBalance = pointAccountDAO.getAccountDetailsByMemberId(fromMemberId);
			//get the point balance details of To memebr ID
			List<PointAccountBalance> toPointAccountBalance = pointAccountDAO.getAccountDetailsByMemberIdWithLock(toMemberId);
			if(toPointAccountBalance != null && toPointAccountBalance.size()>0){
				for (PointAccountBalance fromPointBalance : fromPointAccountBalance) {
					PointAccountType pointAccount = new PointAccountType();
					PointAccountBalance toPointBalance = new PointAccountBalance();
					Boolean flag = false;
					for (PointAccountBalance toPointBalanceTemp : toPointAccountBalance) {
						//there is no concept for credit or debit type in case of Merge
						toPointBalance = toPointBalanceTemp;
						if(fromPointBalance.getPointAccountType().equalsIgnoreCase(toPointBalance.getPointAccountType())){
							flag = true;
							break;
						}
					}
					if(flag){
						//get the point details of To account
						List<Integer> minValueToAdd = new ArrayList<Integer>(3);
						pointAccount = cacheService.getAccountType(toPointBalance.getPointAccountType());
						//get the point Balance Data of To and FROM Account Daily
						if (fromPointBalance.getPointBalance()+toPointBalance.getDailyBonusCreditQty()>pointAccount.getDailyBonusLimitQty()){
							minValueToAdd.add(pointAccount.getDailyBonusLimitQty() - toPointBalance.getDailyBonusCreditQty());
						}else{
							minValueToAdd.add(fromPointBalance.getPointBalance());
						}
						//get the point Balance Data of To and FROM Account Weekly
						if (fromPointBalance.getPointBalance()+toPointBalance.getWeekBonusCreditQty()>pointAccount.getWeekBonusLimitQty()){
							minValueToAdd.add(pointAccount.getWeekBonusLimitQty() - toPointBalance.getWeekBonusCreditQty());
						}else{
							minValueToAdd.add(fromPointBalance.getPointBalance());
						}
						//get the point Balance Data of To and FROM Account Yearly
						if (fromPointBalance.getPointBalance()+toPointBalance.getYearlyCreditQty()>pointAccount.getYearCreditLimitQty()){
							minValueToAdd.add(pointAccount.getYearCreditLimitQty()- toPointBalance.getYearlyCreditQty());
						}else{
							minValueToAdd.add(fromPointBalance.getPointBalance());
						}
						//which ever is lower of yearly,weekly,daily we have to add that amount in all Qty
						int minPointBalance = minValueToAdd.get(0);
						int i = 0;
					    for (int j = i; j < minValueToAdd.size(); j++) {
					    	 if(minValueToAdd.get(j) < minPointBalance){
						            minPointBalance = minValueToAdd.get(j);
						        }
						}
					    //Checking this minimum point balance value for the Balance Limit Qty for that account after obtaining the min value from all the limits
					    if(minPointBalance+toPointBalance.getPointBalance()> pointAccount.getBalanceLimitQty()){
					    	minPointBalance = pointAccount.getBalanceLimitQty() - toPointBalance.getPointBalance();
					    }
					    logger.info("The lowest value is "+minPointBalance+" that will be ADDED during the BONUS merge to the Member "+ toMemberId);
					    	//add for all
					    	logger.trace("adding BONUS Credit Points Qty for all from Member "+ fromMemberId);
						    toPointBalance.setDailyBonusCreditQty(minPointBalance+toPointBalance.getDailyBonusCreditQty());
						    toPointBalance.setWeekBonusCreditQty(minPointBalance+toPointBalance.getWeekBonusCreditQty());
						    toPointBalance.setYearlyCreditQty(minPointBalance+toPointBalance.getYearlyCreditQty());

						    toPointBalance.setPointBalance(minPointBalance+toPointBalance.getPointBalance());

					    //setting the last activity date based on the one that is recent
					    if(fromPointBalance.getLastActivityDt() != null){
					    	if(toPointBalance.getLastActivityDt() == null || toPointBalance.getLastActivityDt().before(fromPointBalance.getLastActivityDt())){
						    	toPointBalance.setLastActivityDt(fromPointBalance.getLastActivityDt());
						    }
					    }
					  if(minPointBalance>0){
					  //update the To member 
					    pointAccountDAO.updateAccountBalanceForMerge(toPointBalance);
					  //Recording the Merge Transaction in points transaction Table for the Trace.
					    logPointsMergedInPointHistory(minPointBalance,fromPointBalance.getPointAccountType(),toMemberId,toPointBalance);
					  }else{
						  logger.info("Minimun Value to add is 0 Hence Points are not logged into PointTrans and Member Account data Remains Unaffected");
					  }
					    minValueToAdd.clear();
					}else{
						logger.info("Merging the Lite Account To FUll Account But the Full Account Has no Such Matching Account Type "+fromPointBalance.getPointAccountType() + "Lite Member ID =>  "+fromPointBalance.getMemberId());
						mergeBonusLitToFullWhereFullIsNull(fromPointBalance,toMemberId);
					}
				}
			}else{
				//add data freshly.
				for (PointAccountBalance fromPointBalance : fromPointAccountBalance) {
					mergeBonusLitToFullWhereFullIsNull(fromPointBalance,toMemberId);
				}
			}
		}

		public void mergeBonusLitToFullWhereFullIsNull(PointAccountBalance fromPointBalance,BigInteger toMemberId){
				logger.info("Its a new Addition of points for the Account "+ fromPointBalance.getPointAccountType());
				PointAccountType pointAccount = new PointAccountType();
				PointAccountBalance toPointBalance = new PointAccountBalance();
					
				//get the point details of from Account Type and match it with the available account Types in DB and insert AS with limit check
				pointAccount = cacheService.getAccountType(fromPointBalance.getPointAccountType());
				if(pointAccount != null){
					
					//get the point details of To account
					List<Integer> minValueToAdd = new ArrayList<Integer>(3);
					//get the point Balance Data of To and FROM Account Daily
					if (fromPointBalance.getPointBalance()>pointAccount.getDailyBonusLimitQty()){
						minValueToAdd.add(pointAccount.getDailyBonusLimitQty());
					}else{
						minValueToAdd.add(fromPointBalance.getPointBalance());
					}
					//get the point Balance Data of To and FROM Account Weekly
					if (fromPointBalance.getPointBalance()>pointAccount.getWeekBonusLimitQty()){
						minValueToAdd.add(pointAccount.getWeekBonusLimitQty());
					}else{
						minValueToAdd.add(fromPointBalance.getPointBalance());
					}
					//get the point Balance Data of To and FROM Account Yearly
					if (fromPointBalance.getPointBalance()>pointAccount.getYearCreditLimitQty()){
						minValueToAdd.add(pointAccount.getYearCreditLimitQty());
					}else{
						minValueToAdd.add(fromPointBalance.getPointBalance());
					}
					//which ever is lower of yearly,weekly,daily we have to add that amount in all Qty
					int minPointBalance = minValueToAdd.get(0);
				
				    for (int j = 0; j < minValueToAdd.size(); j++) {
				    	 if(minValueToAdd.get(j) < minPointBalance){
					            minPointBalance = minValueToAdd.get(j);
					        }
					}
				    //Checking this minimum point balance value for the Balance Limit Qty for that account after obtaining the min value from all the limits
				    if(minPointBalance+toPointBalance.getPointBalance()> pointAccount.getBalanceLimitQty()){
				    	minPointBalance = pointAccount.getBalanceLimitQty() - toPointBalance.getPointBalance();
				    }
				    logger.info("The lowest value is "+minPointBalance+" that will be ADDED during the merge to the Member "+ toMemberId);
				    	//add for all
				    logger.trace("adding Credit Points Qty for all from Member "+ fromPointBalance.getMemberId());
					toPointBalance.setDailyBonusCreditQty(minPointBalance+toPointBalance.getDailyBonusCreditQty());
					toPointBalance.setWeekBonusCreditQty(minPointBalance+toPointBalance.getWeekBonusCreditQty());
					toPointBalance.setYearlyCreditQty(minPointBalance+toPointBalance.getYearlyCreditQty());
							
				    toPointBalance.setPointBalance(minPointBalance+toPointBalance.getPointBalance());
				    toPointBalance.setMemberId(toMemberId);
				    toPointBalance.setPointAccountType(fromPointBalance.getPointAccountType());
				    toPointBalance.setLastActivityDt(fromPointBalance.getLastActivityDt());
				    toPointBalance.setInsertDt(fromPointBalance.getInsertDt());
				    //update the To member 
				    if(minPointBalance>0){
					    pointAccountDAO.insertAccountBalanceForMerge(toPointBalance);
					  //Recording the Merge Transaction in points transaction Table for the Trace.
					    logPointsMergedInPointHistory(fromPointBalance.getPointBalance(),fromPointBalance.getPointAccountType(),toMemberId,toPointBalance);
					 }else{
						  logger.info("Minimun Value to add is 0 Hence Points are not logged into PointTrans and Member Account data Remains Unaffected");
					  }
				}
				
		}
	/**
	 * Helper method for points balance service.
	 * 
	 * @param pointsBalReq
	 * @return
	 * @throws BadRequestException
	 */
	public PointsBalanceResponse getPointsBalance(
			PointsBalanceRequest pointsBalReq) throws BadRequestException {
		// Response
		PointsBalanceResponse pointsBalResp = null;

		// Validating request
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(pointsBalReq,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("getPointsBalance :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		try {
			long start = System.nanoTime();
			pointsBalResp = pointAccountDAO.getPointsBalance(pointsBalReq);
			//if the output is null that means it might be a new memberType
			if (pointsBalResp == null) {
				//check if that is a valid member or not 
				throw new BadRequestException(ErrorCode.GEN_INVALID_ARGUMENT_DESC, ErrorCode.MEMBER_NOT_FOUND);
				
			}else if(pointsBalResp.getPointsExpirationDate() == null){
				//if the member exists but the data doesnot exists
				PointsBalanceResponse newUserResponse = new PointsBalanceResponse();
				newUserResponse.setWeeklyRegularLimit(pointsBalResp.getWeeklyRegularLimit());
				pointsBalResp =newUserResponse ;
				
			}
			
			long end = System.nanoTime();
            long elapsedTime = end - start;
            double seconds = (double) elapsedTime / 1000000000.0;
            logger.info("===========Time Taken by getPointsBalance ============> "+String.valueOf(seconds));			
		} catch (DataIntegrityViolationException rex) {
			logger.trace("getPointsBalance API :", rex);
			throw new BadRequestException(rex.getMessage());
		} catch (BadSqlGrammarException rex) {
			logger.trace("getPointsBalance API :", rex);
			throw new BadRequestException(rex.getMessage());
		}

		// No data found
		

		logger.info("Response from getPointsBalance API : " + pointsBalResp);
		return pointsBalResp;
	}
	
	/**
	 * Helper method for points history service.
	 * @param pointsHistoryReq
	 * @return
	 * @throws BadRequestException
	 */
	public PointsHistoryResponse getPointsHistory(
			PointsHistoryRequest pointsHistoryReq) throws BadRequestException {
		// Response
		PointsHistoryResponse pointsHistoryResp = new PointsHistoryResponse();

		// Validating request
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(pointsHistoryReq,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("getPointsHistory :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		// Parsing String to Date
		Date fromDate = null;
		Date toDate = null;
		
		SimpleDateFormat sdf1 = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			fromDate = sdf1.parse(pointsHistoryReq.getStartDateTime());
			toDate = sdf1.parse(pointsHistoryReq.getEndDateTime());

			pointsHistoryReq.setStartDateTime(sdf2.format(fromDate));
			pointsHistoryReq.setEndDateTime(sdf2.format(toDate));
		} catch (ParseException e) {
			logger.info("getPointsHistory : ParseException : "
					+ pointsHistoryReq.getStartDateTime() + " & "
					+ pointsHistoryReq.getEndDateTime());
		}

		// startDate > endDate
		if (fromDate.after(toDate)) {
			throw new BadRequestException(ErrorCode.DATE_ERROR_MESSAGE,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		try {
			//999 means its an unlimited search days., if its other than 999 then execute the day check block.
				if (!JmxConfigurationProperty.getMaxDateForHistory().equals("999")) {
					long diff = toDate.getTime() - fromDate.getTime();
					long daysDiff = TimeUnit.DAYS.convert(diff,
							TimeUnit.MILLISECONDS);
					logger.info("DaysDiff between startDate and endDate : "+daysDiff);
					if ((daysDiff + 1) > Long.parseLong(JmxConfigurationProperty
							.getMaxDateForHistory())) {
						throw new BadRequestException(
								ErrorCode.DURATION_EXCEEDED_ERROR_MESSAGE,
								ErrorCode.GEN_INVALID_ARGUMENT);
					}
				} 
				List<PointsHistory>	ptsHistoryList = pointAccountDAO.getPointsHistory(pointsHistoryReq);
				if(ptsHistoryList != null && ptsHistoryList.size()>0){
					if(ptsHistoryList.size() >= CSRConstants.MAX_RECORDS_FOR_POINTS){
						pointsHistoryResp.setMaximumRecords(CSRConstants.MAX_RECORDS);
						pointsHistoryResp.setTransaction(ptsHistoryList.subList(0, CSRConstants.MAX_RECORDS_FOR_POINTS));
						return pointsHistoryResp;
					}
				}				
				pointsHistoryResp.setMaximumRecords(null);
				pointsHistoryResp.setTransaction(ptsHistoryList);
		} catch (DataIntegrityViolationException rex) {
			logger.trace("getPointsHistory API :", rex);
			throw new BadRequestException(rex.getMessage());
		} catch (BadSqlGrammarException rex) {
			logger.trace("getPointsHistory API :", rex);
			throw new BadRequestException(rex.getMessage());
		}

//		// No data found
//		if (pointsHistoryResp == null) {
//			logger.trace("getPointsHistory API : pointsHistoryResp : ",
//					pointsHistoryResp);
//			pointsHistoryResp = new PointsHistoryResponse();			
//		}

		logger.info("Response from getPointsHistory API : " + pointsHistoryResp);
		return pointsHistoryResp;
	}
	
	
	
	/**
	 * Helper method for points history service.
	 * @param pointsHistoryReq
	 * @return
	 * @throws BadRequestException
	 */
	public PointsHistoryResponse getPointsOldHistory(
			PointsHistoryRequest pointsHistoryReq) throws BadRequestException {
		// Response
		PointsHistoryResponse pointsHistoryResp = new PointsHistoryResponse();

		// Validating request
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(pointsHistoryReq,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("getPointsHistory :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		// Parsing String to Date
		Date fromDate = null;
		Date toDate = null;
		
		SimpleDateFormat sdf1 = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			fromDate = sdf1.parse(pointsHistoryReq.getStartDateTime());
			toDate = sdf1.parse(pointsHistoryReq.getEndDateTime());

			pointsHistoryReq.setStartDateTime(sdf2.format(fromDate));
			pointsHistoryReq.setEndDateTime(sdf2.format(toDate));
		} catch (ParseException e) {
			logger.info("getPointsHistory : ParseException : "
					+ pointsHistoryReq.getStartDateTime() + " & "
					+ pointsHistoryReq.getEndDateTime());
		}

		// startDate > endDate
		if (fromDate.after(toDate)) {
			throw new BadRequestException(ErrorCode.DATE_ERROR_MESSAGE,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		try {
			//999 means its an unlimited search days., if its other than 999 then execute the day check block.
				if (!JmxConfigurationProperty.getMaxDateForHistory().equals("999")) {
					long diff = toDate.getTime() - fromDate.getTime();
					long daysDiff = TimeUnit.DAYS.convert(diff,
							TimeUnit.MILLISECONDS);
					logger.info("DaysDiff between startDate and endDate : "+daysDiff);
					if ((daysDiff + 1) > Long.parseLong(JmxConfigurationProperty
							.getMaxDateForHistory())) {
						throw new BadRequestException(
								ErrorCode.DURATION_EXCEEDED_ERROR_MESSAGE,
								ErrorCode.GEN_INVALID_ARGUMENT);
					}
				} 
				List<PointsHistory>	ptsHistoryList = pointAccountDAO.getPointsOLDHistory(pointsHistoryReq);
				if(ptsHistoryList != null && ptsHistoryList.size()>0){
					if(ptsHistoryList.size() >= CSRConstants.MAX_RECORDS_FOR_POINTS){
						pointsHistoryResp.setMaximumRecords(CSRConstants.MAX_RECORDS);
						pointsHistoryResp.setTransaction(ptsHistoryList.subList(0, CSRConstants.MAX_RECORDS_FOR_POINTS));
						return pointsHistoryResp;
					}
				}				
				pointsHistoryResp.setMaximumRecords(null);
				pointsHistoryResp.setTransaction(ptsHistoryList);
		} catch (DataIntegrityViolationException rex) {
			logger.trace("getPointsHistory API :", rex);
			throw new BadRequestException(rex.getMessage());
		} catch (BadSqlGrammarException rex) {
			logger.trace("getPointsHistory API :", rex);
			throw new BadRequestException(rex.getMessage());
		}

//		// No data found
//		if (pointsHistoryResp == null) {
//			logger.trace("getPointsHistory API : pointsHistoryResp : ",
//					pointsHistoryResp);
//			pointsHistoryResp = new PointsHistoryResponse();			
//		}

		logger.info("Response from getPointsOLDHistory API : " + pointsHistoryResp);
		return pointsHistoryResp;
	}
	
	/**
	 * Helper method for points balance service.
	 * 
	 * @param pointsBalReq
	 * @return
	 * @throws BadRequestException
	 */
	public PointsBalanceResponseV2 getPointsBalanceV2(
			PointsBalanceRequestV2 pointsBalReq) throws BadRequestException {
		// Response
		PointsBalanceResponseV2 pointsBalResp = null;

		// Validating request
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(pointsBalReq,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("getPointsBalance :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		try {
			long start = System.nanoTime();
			pointsBalResp = pointAccountDAO.getPointsBalanceV2(pointsBalReq);
			//if the output is null that means it might be a new memberType
			if (pointsBalResp == null) {
				//check if that is a valid member or not 
				//throw new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND_DESC, ErrorCode.MEMBER_NOT_FOUND);
				pointsBalResp =pointAccountDAO.getPointsBalanceV2ForNewMember(pointsBalReq);
				pointsBalResp.setMemberId(pointsBalReq.getMemberId().toString());
			}
				List <AccountConfiguration> ac = pointsBalResp.getAccountConfiguration();
				
				for (AccountConfiguration acctConfig : ac) {
					List<StatusLevels> sl = new ArrayList<>();
					
					if(acctConfig.getLevel_1_name() != null && acctConfig.getLevel_1_limit() != null){
						StatusLevels s1 = new StatusLevels();
						s1.setStatusLevelLimit(acctConfig.getLevel_1_limit());
						s1.setStatusLevelName(acctConfig.getLevel_1_name());
						s1.setStatusLevelOrder("1");
						sl.add(s1);
					}
					if(acctConfig.getLevel_2_name() != null && acctConfig.getLevel_2_limit() != null){
						StatusLevels s2 = new StatusLevels();
						s2.setStatusLevelLimit(acctConfig.getLevel_2_limit());
						s2.setStatusLevelName(acctConfig.getLevel_2_name());
						s2.setStatusLevelOrder("2");
						sl.add(s2);
					}
					if(acctConfig.getLevel_3_name() != null && acctConfig.getLevel_3_limit() != null){
						StatusLevels s3 = new StatusLevels();
						s3.setStatusLevelLimit(acctConfig.getLevel_3_limit());
						s3.setStatusLevelName(acctConfig.getLevel_3_name());
						s3.setStatusLevelOrder("3");
						sl.add(s3);
					}
					if(acctConfig.getLevel_4_name() != null && acctConfig.getLevel_4_limit() != null){
						StatusLevels s4 = new StatusLevels();
						s4.setStatusLevelLimit(acctConfig.getLevel_4_limit());
						s4.setStatusLevelName(acctConfig.getLevel_4_name());
						s4.setStatusLevelOrder("4");
						sl.add(s4);
					}
					if(acctConfig.getLevel_5_name() != null && acctConfig.getLevel_5_limit() != null){
						StatusLevels s5 = new StatusLevels();
						s5.setStatusLevelLimit(acctConfig.getLevel_5_limit());
						s5.setStatusLevelName(acctConfig.getLevel_5_name());
						s5.setStatusLevelOrder("5");
						sl.add(s5);
					}
					acctConfig.setStatusLevels(sl);
					
				List<AccountBalance> accountBalanceList = pointsBalResp.getAccountBalaces();
				List<AccountConfiguration> accountConfigurationsList = pointsBalResp.getAccountConfiguration();
				for (AccountBalance accountBalance : accountBalanceList) {
					for (AccountConfiguration accountConfiguration : accountConfigurationsList) {
						if((accountConfiguration.getPointAccountType().equals(accountBalance.getPointAccountType())) && (accountBalance.getTotalBalance() >= 0)){
							
							if((accountConfiguration.getLevel_1_limit() !=null )&& (accountBalance.getTotalBalance() < Integer.parseInt(accountConfiguration.getLevel_1_limit()))){
								accountBalance.setStatusNextLevel(accountConfiguration.getLevel_1_name());
								int pointToReachNextLevel = Integer.parseInt(accountConfiguration.getLevel_1_limit()) - accountBalance.getTotalBalance();
								accountBalance.setStatusPointsToNextLevel(Integer.toString(pointToReachNextLevel));
								
							}else if ((accountConfiguration.getLevel_2_limit() !=null ) &&(accountConfiguration.getLevel_1_limit() !=null )&&(accountBalance.getTotalBalance() >= Integer.parseInt(accountConfiguration.getLevel_1_limit()) &&  accountBalance.getTotalBalance() < Integer.parseInt(accountConfiguration.getLevel_2_limit()))){
								accountBalance.setStatusNextLevel(accountConfiguration.getLevel_2_name());
								int pointToReachNextLevel = Integer.parseInt(accountConfiguration.getLevel_2_limit()) - accountBalance.getTotalBalance();
								accountBalance.setStatusPointsToNextLevel(Integer.toString(pointToReachNextLevel));
								
							}else if ((accountConfiguration.getLevel_2_limit() !=null ) &&(accountConfiguration.getLevel_3_limit() !=null )&&(accountBalance.getTotalBalance() >= Integer.parseInt(accountConfiguration.getLevel_2_limit()) &&  accountBalance.getTotalBalance() < Integer.parseInt(accountConfiguration.getLevel_3_limit()))){
								accountBalance.setStatusNextLevel(accountConfiguration.getLevel_3_name());
								int pointToReachNextLevel = Integer.parseInt(accountConfiguration.getLevel_3_limit()) - accountBalance.getTotalBalance();
								accountBalance.setStatusPointsToNextLevel(Integer.toString(pointToReachNextLevel));
								
							}else if ((accountConfiguration.getLevel_3_limit() !=null ) &&(accountConfiguration.getLevel_4_limit() !=null )&&(accountBalance.getTotalBalance() >= Integer.parseInt(accountConfiguration.getLevel_3_limit()) &&  accountBalance.getTotalBalance() < Integer.parseInt(accountConfiguration.getLevel_4_limit()))){
								accountBalance.setStatusNextLevel(accountConfiguration.getLevel_4_name());
								int pointToReachNextLevel = Integer.parseInt(accountConfiguration.getLevel_4_limit()) - accountBalance.getTotalBalance();
								accountBalance.setStatusPointsToNextLevel(Integer.toString(pointToReachNextLevel));
								
							}else if ((accountConfiguration.getLevel_4_limit() !=null ) &&(accountConfiguration.getLevel_5_limit() !=null )&&(accountBalance.getTotalBalance() >= Integer.parseInt(accountConfiguration.getLevel_4_limit()) &&  accountBalance.getTotalBalance() < Integer.parseInt(accountConfiguration.getLevel_5_limit()))){
								accountBalance.setStatusNextLevel(accountConfiguration.getLevel_5_name());
								int pointToReachNextLevel = Integer.parseInt(accountConfiguration.getLevel_5_limit()) - accountBalance.getTotalBalance();
								accountBalance.setStatusPointsToNextLevel(Integer.toString(pointToReachNextLevel));
							}else if ((accountConfiguration.getLevel_5_limit() !=null ) &&(accountBalance.getTotalBalance() >= Integer.parseInt(accountConfiguration.getLevel_5_limit()))){
								accountBalance.setStatusNextLevel(accountConfiguration.getLevel_5_name());
								int pointToReachNextLevel = 0;
								accountBalance.setStatusPointsToNextLevel(Integer.toString(pointToReachNextLevel));
							}
							
							break;
						}
					}
				}
			}		
			long end = System.nanoTime();
            long elapsedTime = end - start;
            double seconds = (double) elapsedTime / 1000000000.0;
            logger.info("===========Time Taken by getPointsBalance ============> "+String.valueOf(seconds));			
		} catch (DataIntegrityViolationException rex) {
			logger.trace("getPointsBalance API :", rex);
			throw new BadRequestException(rex.getMessage());
		} catch (BadSqlGrammarException rex) {
			logger.trace("getPointsBalance API :", rex);
			throw new BadRequestException(rex.getMessage());
		}

		// No data found
		

		logger.info("Response from getPointsBalance API V2 : " + pointsBalResp);
		return pointsBalResp;
	}
	
}
