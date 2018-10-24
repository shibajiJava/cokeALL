package com.ko.cds.response.csr;

import java.util.List;

import com.ko.cds.pojo.csr.SessionSearch;

/**
 * Pojo for capturing the Response for the Session Search API
 * @author ibm
 *
 */
public class SessionSearchResponse {
	
	private String maximumRecords = "1";
	
	private List<SessionSearch> Sessions ;

	public String getMaximumRecords() {
		return maximumRecords;
	}

	public void setMaximumRecords(String maximumRecords) {
		this.maximumRecords = maximumRecords;
	}

	public List<SessionSearch>  getSessions() {
		return Sessions;
	}

	public void setSessions(List<SessionSearch>  sessions) {
		this.Sessions = sessions;
	}

	@Override
	public String toString() {
		return "SessionSearchResponse [maximumRecords=" + maximumRecords
				+ ", Sessions=" + Sessions + "]";
	}
	


}
