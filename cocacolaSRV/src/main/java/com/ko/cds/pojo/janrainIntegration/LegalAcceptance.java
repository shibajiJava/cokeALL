package com.ko.cds.pojo.janrainIntegration;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

public class LegalAcceptance {
	private Map<String, String> other = new HashMap<String, String>();
	private String legalAcceptanceId;
	private String clientId;
	private BigInteger id;
	private String screenName;
	private String type;
	private String accepted;
	private String dateAccepted;
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
	public String getLegalAcceptanceId() {
		return legalAcceptanceId;
	}
	public void setLegalAcceptanceId(String legalAcceptanceId) {
		this.legalAcceptanceId = legalAcceptanceId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccepted() {
		return accepted;
	}
	public void setAccepted(String accepted) {
		this.accepted = accepted;
	}
	public String getDateAccepted() {
		return dateAccepted;
	}
	public void setDateAccepted(String dateAccepted) {
		this.dateAccepted = dateAccepted;
	}
	
	
}
