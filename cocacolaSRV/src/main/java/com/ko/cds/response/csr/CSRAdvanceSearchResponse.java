package com.ko.cds.response.csr;

import java.io.Serializable;
import java.util.List;

import com.ko.cds.pojo.csr.CSRMemberInfo;

/**
 * This class is used to populate JSON response for advance search API
 * 
 * @author ibm
 *
 */

public class CSRAdvanceSearchResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4806125383520226742L;
	
	private  List<CSRMemberInfo> csrMembers;
	/** Return “Maximum” if the response if too high */
	private String maximumRecords;
	public List<CSRMemberInfo> getCsrMembers() {
		return csrMembers;
	}
	public void setCsrMembers(List<CSRMemberInfo> csrMembers) {
		this.csrMembers = csrMembers;
	}
	public String getMaximumRecords() {
		return maximumRecords;
	}
	public void setMaximumRecords(String maximumRecords) {
		this.maximumRecords = maximumRecords;
	}
	
	

}
