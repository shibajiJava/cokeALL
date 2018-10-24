package com.ko.cds.sch.rollbackpoints;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.pojo.points.AccountBalancePK;
import com.ko.cds.pojo.points.HoldTypeTransactionBean;
import com.ko.cds.pojo.points.PointAccountBalance;
import com.ko.cds.pojo.points.PointAccountType;
import com.ko.cds.service.helper.ICacheService;
import com.ko.cds.service.helper.PointAccountServiceHelper;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;

/*
 * This Class will take care of Crediting the Points that has been debited during the debit Points API debit Type hold.
 * Such that the Debited points are not being confirmed by the member in the 240 seconds time. 
 * We dont have to consider the case of confirm and Regualr debitType.
 */
@Component
public class DebitedPointCreditHelper {
	
	 private static final Logger logger = LoggerFactory.getLogger("pointsRollbackSchedularTask");
	 
	@Autowired
	private PointAccountDAO pointAccountDAO; 
	@Autowired
	private SequenceNumberDAO sequenceNumberDAO; 
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private PointAccountServiceHelper pointAccountServiceHelper;
	
	@Transactional
	public  void execute(){
		List<HoldTypeTransactionBean> holdTypetransList = new ArrayList<HoldTypeTransactionBean>();
		HoldTypeTransactionBean holdBean = new HoldTypeTransactionBean();
		holdBean.setPointsType(CDSConstants.DEBIT_TYPE_HOLD);
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		//holdBean.setStartTime(configProp.get("startTimeFrame"));
	    //holdBean.setEndTime(configProp.get("endTimeFrame"));
		//get the set of records of debitType 'HOLD' for processing let say one day record.
		holdTypetransList = pointAccountDAO.getHoldTypeTransactionRecords(holdBean);
		//update the inprogress as the typefor the DebitType
		
		if (null != holdTypetransList && holdTypetransList.size() > 0) {
		/*	
			for (HoldTypeTransactionBean holdTypeTransactionBean : holdTypetransList) {
				//reset from in progress to hold
				logger.info("Setting the type as In progress  for the member Id => "+holdTypeTransactionBean.getMemberId()+" and point trans Id As => "+ holdTypeTransactionBean.getPointTransId());
				holdTypeTransactionBean.setPointsType(CDSConstants.DEBIT_TYPE_INPROGRESS);
				String transactionTimeStampString = CDSOUtils.formatDateDBDate(seq.getCurrentDBTime())
				holdTypeTransactionBean.setLastTransactionDate(transactionTimeStampString);
				pointAccountDAO.updateHoldPointTypeHistory(holdTypeTransactionBean);
			}
		*/	
			for (HoldTypeTransactionBean holdTypeTransactionBean : holdTypetransList) {
				//get the insert date and update date of that record.
				logger.info("First Insert Time for this transaction "+holdTypeTransactionBean.getPointTransId()+" is "+holdTypeTransactionBean.getInsertDate().getTime());
				//System.out.println("Second Insert Time for this transaction "+holdTypeTransactionBean.getPointTransId()+" is "+holdTypeTransactionBean.getInsertDate().getTime());
				//get the time from DB for the current time for each of the Hold data type...
				Timestamp currentTimstamp = pointAccountDAO.getCurrentDBTime();
				PointAccountType pointAccountType = cacheService
						.getAccountType(holdTypeTransactionBean.getAccountType());
				//check if the insert date and the update date is >240Seconds difference 
				double rollbackDefaultTime=Double.parseDouble(configProp.get("rollBackTime"));
				int holdTime;
				if(holdTypeTransactionBean.getHoldTime() == null || holdTypeTransactionBean.getHoldTime()==0){
					holdTime=(int)rollbackDefaultTime;
				}else{
					holdTime =holdTypeTransactionBean.getHoldTime();
				}
				if(holdTypeTransactionBean.getInsertDate().getTime()/1000+holdTime <=currentTimstamp.getTime()/1000){
					logger.info("Second Insert Time for this transaction "+holdTypeTransactionBean.getPointTransId()+" is "+holdTypeTransactionBean.getInsertDate().getTime());
				//if(currentTimstamp.getTime()/1000-holdTypeTransactionBean.getInsertDate().getTime()/1000 >= Double.parseDouble(configProp.get("rollBackTime"))){
					//the hold time is still >= 240 seconds means we have to credit the points again back to the MAB table.
					AccountBalancePK accountBalancePK = new AccountBalancePK();
					accountBalancePK.setMemberId(holdTypeTransactionBean.getMemberId());
					accountBalancePK.setPointAccountType(holdTypeTransactionBean.getAccountType());
					//get the MAB account details
					PointAccountBalance pointBalanceAccount = pointAccountDAO.getAccountDetailsWithLock(accountBalancePK);
					//credit the debited points.
					pointBalanceAccount.setPointBalance(pointBalanceAccount
							.getPointBalance() + holdTypeTransactionBean.getPointsQuanity());
					String statusLevel = pointAccountServiceHelper.getMemberAccountStatusLevel(pointAccountType,pointBalanceAccount
							.getPointBalance());
					pointBalanceAccount.setStatusLevel(statusLevel);
					pointAccountDAO.updateAccountBalance(pointBalanceAccount);
					//now update the point trans table with the hold type as rolledback.
					holdTypeTransactionBean.setPointsType(CDSConstants.DEBIT_TYPE_ROLLBACK);
					//will re- set the db time stamp and the debit type only.
					String transactionTimeStampString = CDSOUtils.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime());
					holdTypeTransactionBean.setLastTransactionDate(transactionTimeStampString);
					//reset the balance to old value change as per the CSR API Spec
					holdTypeTransactionBean.setBalance(new BigInteger(String.valueOf(pointBalanceAccount.getPointBalance())));
					holdTypeTransactionBean.setStatusLevel(statusLevel);
					pointAccountDAO.updateHoldPointTypeHistory(holdTypeTransactionBean);
					logger.info("Setting the type as RolledBack  for the member Id => "+holdTypeTransactionBean.getMemberId()+" and point trans Id As => "+ holdTypeTransactionBean.getPointTransId());
				}
				/*
				else{
					//reset from in progress to hold
					holdTypeTransactionBean.setPointsType(CDSConstants.DEBIT_TYPE_HOLD);
					String transactionTimeStampString = CDSOUtils.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime());
					holdTypeTransactionBean.setLastTransactionDate(transactionTimeStampString);
					pointAccountDAO.updateHoldPointTypeHistory(holdTypeTransactionBean);
					logger.info("Setting the type as Hold  for the member Id => "+holdTypeTransactionBean.getMemberId()+" and point trans Id As => "+ holdTypeTransactionBean.getPointTransId());
				}*/
			}
		}else{
			logger.info("No Hold Type data found in DB");
		}
	}

}
