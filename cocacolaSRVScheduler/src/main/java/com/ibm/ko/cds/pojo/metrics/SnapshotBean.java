package com.ibm.ko.cds.pojo.metrics;

import java.math.BigInteger;
import java.sql.Timestamp;

/*
 * This class will capture the snapshot bean information , a POJO class for the table monitor_tran.
 */
public class SnapshotBean {
	
	BigInteger monitoringId;
	String apiName ;
	BigInteger monitoringRefId;
	String monitoringName ;
	String monitoringValue    ;
	Timestamp insertDate ;
	String sampleTransTime;
	
	public SnapshotBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SnapshotBean(BigInteger monitoringId, String apiName,
			BigInteger monitoringRefId, String monitoringName,
			String monitoringValue, Timestamp insertDate, String sampleTransTime) {
		super();
		this.monitoringId = monitoringId;
		this.apiName = apiName;
		this.monitoringRefId = monitoringRefId;
		this.monitoringName = monitoringName;
		this.monitoringValue = monitoringValue;
		this.insertDate = insertDate;
		this.sampleTransTime = sampleTransTime;
	}

	/**
	 * @return the monitoringId
	 */
	public BigInteger getMonitoringId() {
		return monitoringId;
	}

	/**
	 * @param monitoringId the monitoringId to set
	 */
	public void setMonitoringId(BigInteger monitoringId) {
		this.monitoringId = monitoringId;
	}

	/**
	 * @return the apiName
	 */
	public String getApiName() {
		return apiName;
	}

	/**
	 * @param apiName the apiName to set
	 */
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	/**
	 * @return the monitoringRefId
	 */
	public BigInteger getMonitoringRefId() {
		return monitoringRefId;
	}

	/**
	 * @param monitoringRefId the monitoringRefId to set
	 */
	public void setMonitoringRefId(BigInteger monitoringRefId) {
		this.monitoringRefId = monitoringRefId;
	}

	/**
	 * @return the monitoringName
	 */
	public String getMonitoringName() {
		return monitoringName;
	}

	/**
	 * @param monitoringName the monitoringName to set
	 */
	public void setMonitoringName(String monitoringName) {
		this.monitoringName = monitoringName;
	}

	/**
	 * @return the monitoringValue
	 */
	public String getMonitoringValue() {
		return monitoringValue;
	}

	/**
	 * @param monitoringValue the monitoringValue to set
	 */
	public void setMonitoringValue(String monitoringValue) {
		this.monitoringValue = monitoringValue;
	}

	/**
	 * @return the insertDate
	 */
	public Timestamp getInsertDate() {
		return insertDate;
	}

	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * @return the sampleTransTime
	 */
	public String getSampleTransTime() {
		return sampleTransTime;
	}

	/**
	 * @param sampleTransTime the sampleTransTime to set
	 */
	public void setSampleTransTime(String sampleTransTime) {
		this.sampleTransTime = sampleTransTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SnapshotBean [monitoringId=" + monitoringId + ", apiName="
				+ apiName + ", monitoringRefId=" + monitoringRefId
				+ ", monitoringName=" + monitoringName + ", monitoringValue="
				+ monitoringValue + ", insertDate=" + insertDate
				+ ", sampleTransTime=" + sampleTransTime + "]";
	}  
	
	
	
}
