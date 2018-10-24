package com.ko.cds.dao.common;

import java.math.BigInteger;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.ko.cds.pojo.common.SequenceNumber;

@Component
public interface SequenceNumberDAO {

	public BigInteger getNextSequenceNumber(SequenceNumber sequenceName);
	
	public String getCurrentDBTime();
}
