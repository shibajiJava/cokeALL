package com.ko.cds.response.csr;

import java.util.List;

import com.ko.cds.pojo.csr.CodeRedemptionHistory;

/**
 * Pojo Object to Capture the Code Redemption History Response
 * @author ibm
 *
 */
public class CodeRedemptionHistoryResponse {
	
	private List<CodeRedemptionHistory> codeRedemptions;
	private String 	maximumRecords ;
	
	
	
	public List<CodeRedemptionHistory> getCodeRedemptions() {
		return codeRedemptions;
	}
	public void setCodeRedemptions(List<CodeRedemptionHistory> codeRedemptions) {
		this.codeRedemptions = codeRedemptions;
	}
	public String getMaximumRecords() {
		return maximumRecords;
	}
	public void setMaximumRecords(String maximumRecords) {
		this.maximumRecords = maximumRecords;
	}
	@Override
	public String toString() {
		return "CodeRedemptionHistoryResponse [codeRedemptions="
				+ codeRedemptions + ", maximumRecords=" + maximumRecords + "]";
	}


}
