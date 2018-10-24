package com.ko.cds.dao.activity;

import java.math.BigInteger;
import java.util.List;

import com.ko.cds.pojo.activity.ActivityTrans;
import com.ko.cds.pojo.activity.RedemptionTrans;
import com.ko.cds.pojo.csr.ActivitySearch;
import com.ko.cds.pojo.csr.SessionSearch;
import com.ko.cds.pojo.csr.Site;
import com.ko.cds.request.activity.RecordSessionInfoRequest;
import com.ko.cds.request.csr.ActivitySearchRequest;
import com.ko.cds.request.csr.SessionSearchRequest;

/**
 * DAO Interface for Activity APIs
 * @author IBM
 *
 */
public interface ActivityDAO {
	public int recordSessionInfo(RecordSessionInfoRequest recordSessionInfoRequest);
	public int bunchBallInfo(ActivityTrans activityTrans);
	public BigInteger recordRedemption(RedemptionTrans redemptionTrans);
	public BigInteger getTransId(BigInteger memberId);
	public int recordMemberSite(RecordSessionInfoRequest recordSessionInfoRequest);
	public Site getMemberSiteRecord(RecordSessionInfoRequest recordSessionInfoRequest);
	public List<ActivitySearch> searchActivity(ActivitySearchRequest activitySearchRequest);
	public List<ActivitySearch> searchActivityRev(ActivitySearchRequest activitySearchRequest);
	public List<SessionSearch> searchSession(SessionSearchRequest sessionSearchRequest);
	public BigInteger getSiteId(BigInteger siteId);
	public BigInteger getMemberId(BigInteger memberId);
}
