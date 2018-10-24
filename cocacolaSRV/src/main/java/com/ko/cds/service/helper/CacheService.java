package com.ko.cds.service.helper;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.pojo.points.PointAccountType;

@Component
public class CacheService implements ICacheService {

	@Autowired
	private PointAccountDAO pointAccountDAO;
	@Autowired
	private SessionIdDAO sessionIdDAO;
	@Autowired
	private MemberDAO memberDAO;
	public static final Logger logger = LoggerFactory.getLogger(CacheService.class);
	@Override
	public PointAccountType getAccountType(String accountType) {
		PointAccountType pointAccountType=pointAccountDAO.getAccountType(accountType);
		logger.info("getAccountType :"+accountType);
		return pointAccountType;
	}
	@Override
	public BigInteger getSessionID(String sessionUuid) {
		BigInteger sessionId = sessionIdDAO.getSessionID(sessionUuid);
		logger.info(" getSessionID : "+ sessionId);
		return sessionId;
	}
	@Override
	public BigInteger getMemberIdByUUID(String janrainUUID) {
		BigInteger memberId = memberDAO.getMemberIdbyJanrainUUID(janrainUUID);
		return memberId;
	}
	
}
