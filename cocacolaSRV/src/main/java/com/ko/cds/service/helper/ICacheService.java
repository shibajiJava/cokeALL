package com.ko.cds.service.helper;

import java.math.BigInteger;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ko.cds.pojo.points.PointAccountType;
@Component
public interface ICacheService {
	@Cacheable(value="CDSAccountTypes", key="#accountType")
	public PointAccountType getAccountType(String accountType);
	@Cacheable(value="CDSSessionIds", key="#sessionUuid")
	public BigInteger getSessionID(String sessionUuid);
	@Cacheable(value="CDSMemberId", key="#janrainUUID")
	public BigInteger getMemberIdByUUID(String janrainUUID);
	
}
