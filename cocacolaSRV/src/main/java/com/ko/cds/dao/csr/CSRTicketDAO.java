package com.ko.cds.dao.csr;

import java.util.List;

import com.ko.cds.pojo.csr.CSRTicket;
import com.ko.cds.request.csr.CSRTicketGetRequest;
import com.ko.cds.request.csr.CSRTicketPostRequest;

public interface CSRTicketDAO {
	public void insertCSRTicket(CSRTicketPostRequest cSRTicketPostRequest);
	public List<CSRTicket> getCSRHistory(CSRTicketGetRequest cSRTicketGetRequest);

}
