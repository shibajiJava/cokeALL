package com.ko.cds.request.csr;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.MaxLength;

/**
 * Pojo Object to Capture the Session Search Request
 * @author ibm
 *
 */
public class SessionSearchRequest {
	
	@QueryParam("sessionUUID")
	@MaxLength(value = 36)
	private String sessionUUID ;
	
	@QueryParam("memberId")
	private BigInteger 	memberId;
	
	@QueryParam("sessionDateSearchStart")
	private String sessionDateSearchStart ;	
	
	@QueryParam("sessionDateSearchEnd")
	private String sessionDateSearchEnd;
	
	@QueryParam("ipAddress")
	@MaxLength(value = 20)
	private String ipAddress ;
	
	@QueryParam("siteId")
	private BigInteger 	siteId ;
	
	@QueryParam("searchSessionUUID")
	@MaxLength(value = 36)
	private String 	searchSessionUUID ;
	
	
	public String getSessionUUID() {
		return sessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getSessionDateSearchStart() {
		if (sessionDateSearchStart != null && sessionDateSearchStart.contains("T")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");		
			try {
				Date date1 = format.parse(this.sessionDateSearchStart);
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String outputDate = format.format(date1).toString();
				this.sessionDateSearchStart = outputDate;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return sessionDateSearchStart;
	}
	public void setSessionDateSearchStart(String sessionDateSearchStart) {
		this.sessionDateSearchStart = sessionDateSearchStart;
	}
	public String getSessionDateSearchEnd() {

		if (sessionDateSearchEnd != null && sessionDateSearchEnd.contains("T")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");		
			try {
				Date date1 = format.parse(this.sessionDateSearchEnd);
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String outputDate = format.format(date1).toString();
				this.sessionDateSearchEnd = outputDate;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return sessionDateSearchEnd;
	}
	public void setSessionDateSearchEnd(String sessionDateSearchEnd) {
		this.sessionDateSearchEnd = sessionDateSearchEnd;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public BigInteger getSiteId() {
		return siteId;
	}
	public void setSiteId(BigInteger siteId) {
		this.siteId = siteId;
	}
	public String getSearchSessionUUID() {
		return searchSessionUUID;
	}
	public void setSearchSessionUUID(String searchSessionUUID) {
		this.searchSessionUUID = searchSessionUUID;
	}
	
	
	@Override
	public String toString() {
		return "SessionSearchRequest [sessionUUID=" + sessionUUID
				+ ", memberId=" + memberId + ", sessionDateSearchStart="
				+ sessionDateSearchStart + ", sessionDateSearchEnd="
				+ sessionDateSearchEnd + ", ipAddress=" + ipAddress
				+ ", siteId=" + siteId + ", searchSessionUUID="
				+ searchSessionUUID + "]";
	}


}
