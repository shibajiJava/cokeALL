package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

public class MergedMember implements Serializable{

	/**
	 * @author IBM
	 * 
	 * MergedMember
	 */
	private static final long serialVersionUID = 1L;
	
	private BigInteger mergedFrom;
	private BigInteger mergedTo;
	private Timestamp insertDt;
	public BigInteger getMergedFrom() {
		return mergedFrom;
	}
	public void setMergedFrom(BigInteger mergedFrom) {
		this.mergedFrom = mergedFrom;
	}
	public BigInteger getMergedTo() {
		return mergedTo;
	}
	public void setMergedTo(BigInteger mergedTo) {
		this.mergedTo = mergedTo;
	}
	public Timestamp getInsertDt() {
		return insertDt;
	}
	public void setInsertDt(Timestamp insertDt) {
		this.insertDt = insertDt;
	}
	
	
	

}
