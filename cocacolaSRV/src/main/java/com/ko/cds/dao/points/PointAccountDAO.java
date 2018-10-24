package com.ko.cds.dao.points;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ko.cds.pojo.points.AccountBalancePK;
import com.ko.cds.pojo.points.HoldTypeTransactionBean;
import com.ko.cds.pojo.points.PointAccountBalance;
import com.ko.cds.pojo.points.PointAccountType;
import com.ko.cds.pojo.points.PointsHistory;
import com.ko.cds.pojo.points.TransactionHistory;
import com.ko.cds.request.points.PointsBalanceRequest;
import com.ko.cds.request.points.PointsBalanceRequestV2;
import com.ko.cds.request.points.PointsHistoryRequest;
import com.ko.cds.response.points.PointsBalanceResponse;
import com.ko.cds.response.points.PointsBalanceResponseV2;

@Component
public interface PointAccountDAO {
	public PointAccountBalance getAccountDetails(AccountBalancePK accountBalancePK );
	public PointAccountType getAccountType(String accountType);
	public void insertAccountBalance(PointAccountBalance accountBalance);
	public void insertAccountBalanceForMerge(PointAccountBalance accountBalance);
	public void insertTransactionHistory(TransactionHistory transactionHistory);
	public void updateAccountBalance(PointAccountBalance accountBalance);
	public void updateAccountBalanceForMerge(PointAccountBalance accountBalance);
	public TransactionHistory getPointHistory(BigInteger transactionId);
	public PointAccountBalance getAccountDetailsWithLock(AccountBalancePK accountBalancePK );
	public List<PointAccountBalance> getAccountDetailsByMemberIdWithLock(BigInteger toMemberId);
	public PointsBalanceResponse getPointsBalance(PointsBalanceRequest pointsBalanceReq);
	public List<PointAccountBalance> getAccountDetailsByMemberId(BigInteger memberId);
	public List<TransactionHistory> getPointHistoryByMemberId(TransactionHistory transactionHistory);
	public void insertTransactionHistoryByMemberId(TransactionHistory transactionHistory);
	public void insertTransactionHistoryForMerge(TransactionHistory transactionHistory);
	public TransactionHistory getPointHistoryByTransactionIdWithLock(BigInteger transactionId);
	public void updatePointHistory(TransactionHistory history);
	public List<PointsHistory> getPointsHistory(PointsHistoryRequest pointsHistoryRequest);
	public List<HoldTypeTransactionBean> getHoldTypeTransactionRecords(HoldTypeTransactionBean holdTypeTransactionBean);
	public void updateHoldPointTypeHistory(HoldTypeTransactionBean holdTypeTransactionBean);
	public Timestamp getCurrentDBTime();
	public List<PointsHistory> getPointsOLDHistory(PointsHistoryRequest pointsHistoryRequest);
	public void logCDSACreditProcessDetails(TransactionHistory history);
	public PointsBalanceResponseV2 getPointsBalanceV2(PointsBalanceRequestV2 pointsBalanceReq);
	public PointsBalanceResponseV2 getPointsBalanceV2ForNewMember(PointsBalanceRequestV2 pointsBalanceReq);
}
