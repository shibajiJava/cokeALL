package com.ko.cds.response.record;

import java.io.Serializable;
import java.math.BigInteger;

public class RecordRedemptionResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigInteger transactionId;

	public BigInteger getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(BigInteger transactionId) {
		this.transactionId = transactionId;
	}

	

}
