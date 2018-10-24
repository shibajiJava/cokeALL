package com.ko.cds.dao.sessionId;

import java.math.BigInteger;

public interface SessionIdDAO {
	public BigInteger getSessionID(String sessionUuid);

}
