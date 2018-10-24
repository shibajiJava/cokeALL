package com.ko.cds.etlstagging;

import java.io.Serializable;

public class EtlLoadTracker implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1909775646L;
	
	public String processName;
	public String targetTable;
	public String status;
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getTargetTable() {
		return targetTable;
	}
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "EtlLoadTracker [processName=" + processName + ", targetTable="
				+ targetTable + ", status=" + status + "]";
	}
	
	
	

}
