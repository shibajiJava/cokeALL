package com.ibm.ko.cds.pojo.metrics;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

/*
 * This class will act as POJO for the table Monitor_element , that will capture the monitoring parameters details.
 */
public class MonitorParams implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String paramName;
	
	String paramValue;
	
	String isActive;
	
	BigInteger monitoringId; 
	
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

	private Map<String, String> other = new HashMap<String, String>();

	
	
	public MonitorParams() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MonitorParams(String paramName, String paramVlaue, String isActive) {
		super();
		this.paramName = paramName;
		this.paramValue = paramVlaue;
		this.isActive = isActive;
	}

	/**
	 * @return the paramName
	 */
	public String getParamName() {
		return paramName;
	}

	/**
	 * @param paramName the paramName to set
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * @return the paramVlaue
	 */
	public String getParamValue() {
		return paramValue;
	}

	/**
	 * @param paramVlaue the paramVlaue to set
	 */
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	/**
	 * @return the isActive
	 */
	public String isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(String isActive) {
		this.isActive = isActive;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MonitorParams [paramName=" + paramName + ", paramValue="
				+ paramValue + ", isActive=" + isActive + "]";
	}
	
	
	@JsonAnyGetter
	public Map<String, String> any() {
		return other;
	}

	@JsonAnySetter
	public void set(String name, String value) {
		other.put(name, value);
	}

	public boolean hasUnknowProperties() {
		return !other.isEmpty();
	}

}
