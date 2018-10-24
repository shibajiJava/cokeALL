package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/***
 * 
 * @author IBM
 *
 */

@JsonIgnoreProperties(value = { "janrainCommOptList","insertDateJanrain"})
public class CommunicationOptIn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** member id**/
	private BigInteger memberId;
	/*** communication type name**/
	private String communicationTypeName;
	/***Accepted Date**/
	private Date acceptedDate;
	/** client id**/
	private String clientId;
	/** Opted in Indicator **/
	private String optedInIndicator;
	/** type**/
	private String type;
	/** format **/
	private String format;
	/** Schedule preference**/
	private String schedulePreference;
	
	private Date insertDate;
	
	private Timestamp insertDateJanrain;
	
	public Timestamp getInsertDateJanrain() {
		return insertDateJanrain;
	}
	public void setInsertDateJanrain(Timestamp insertDateJanrain) {
		this.insertDateJanrain = insertDateJanrain;
	}
	private List<CommunicationOptIn> janrainCommOptList;
	
	
	
	public List<CommunicationOptIn> getJanrainCommOptList() {
		return janrainCommOptList;
	}
	public void setJanrainCommOptList(List<CommunicationOptIn> janrainCommOptList) {
		this.janrainCommOptList = janrainCommOptList;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getCommunicationTypeName() {
		return communicationTypeName;
	}
	public void setCommunicationTypeName(String communicationTypeName) {
		this.communicationTypeName = communicationTypeName;
	}
	public Date getAcceptedDate() {
		return acceptedDate;
	}
	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getOptedInIndicator() {
		return optedInIndicator;
	}
	public void setOptedInIndicator(String optedInIndicator) {
		this.optedInIndicator = optedInIndicator;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getSchedulePreference() {
		return schedulePreference;
	}
	public void setSchedulePreference(String schedulePreference) {
		this.schedulePreference = schedulePreference;
	}
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	
	
	
	

}
