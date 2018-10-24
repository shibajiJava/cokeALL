package com.ko.cds.response.csr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ko.cds.pojo.csr.CSRTicket;
/****
 * This class is used to populate CSR ticket history response
 * @author ibm
 *
 */
public class CSRTicketResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4140654213960216845L;
	/** An array of CSR ticket Objects for the specified CDS member */
	List<CSRTicket> csrTickets = new ArrayList<CSRTicket>();
	/** Returns “maximum” if  300 records are returned */
	private String maximumRecords;
	public List<CSRTicket> getCsrTickets() {
		return csrTickets;
	}
	public void setCsrTickets(List<CSRTicket> csrTickets) {
		this.csrTickets = csrTickets;
	}
	public String getMaximumRecords() {
		return maximumRecords;
	}
	public void setMaximumRecords(String maximumRecords) {
		this.maximumRecords = maximumRecords;
	}
	

}
