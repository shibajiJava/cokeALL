package com.ibm.ko.cds.pojo.janrainIntegration;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

public class CommunicationOpt {
	private Map<String, String> other = new HashMap<String, String>();
	private String optId;
	/***Accepted Date**/
	private String dateAccepted;
	/** client id**/
	private String clientId;
	/** Opted in Indicator **/
	private boolean accepted;
	/** type**/
	private String type;
	/** format **/
	private String format;
	/** Schedule preference**/
	private String schedulePreference;
	
	private BigInteger id;
	
	@JsonAnyGetter
	public Map<String, String> any() {
		return other;
	}

	@JsonAnySetter
	public void set(String name, String value) {
		other.put(name, value);
	}

	public boolean hasUnknowProperties() {
		return !other.isEmpty();
	}

	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
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
	public String getOptId() {
		return optId;
	}
	public void setOptId(String optId) {
		this.optId = optId;
	}
	public String getDateAccepted() {
		return dateAccepted;
	}
	public void setDateAccepted(String dateAccepted) {
		this.dateAccepted = dateAccepted;
	}
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	
}
