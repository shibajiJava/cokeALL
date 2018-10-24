package com.ko.cds.response.config;

import java.io.Serializable;
import java.util.List;

import com.ko.cds.pojo.config.ReasonCode;

public class GetReasonCodeResponse implements Serializable{

	
	private static final long serialVersionUID = 1898989L;
	
	private List<ReasonCode> ReasonCodes;

	public List<ReasonCode> getReasonCodes() {
		return ReasonCodes;
	}

	public void setReasonCodes(List<ReasonCode> ReasonCodes) {
		this.ReasonCodes = ReasonCodes;
	}

	
	
}
