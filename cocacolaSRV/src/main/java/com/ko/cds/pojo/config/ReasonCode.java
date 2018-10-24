package com.ko.cds.pojo.config;

import java.io.Serializable;

public class ReasonCode implements Serializable{

	
	String reasonCode;
	
	String reasonDescription;
	
	private static final long serialVersionUID = 190909090L;

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	@Override
	public String toString() {
		return "ReasonCode [reasonCode=" + reasonCode + ", reasonDescription="
				+ reasonDescription + "]";
	}
	
	
}
