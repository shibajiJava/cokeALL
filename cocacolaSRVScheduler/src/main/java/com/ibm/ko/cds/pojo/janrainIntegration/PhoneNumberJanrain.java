package com.ibm.ko.cds.pojo.janrainIntegration;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

public class PhoneNumberJanrain {
	private Map<String, String> other = new HashMap<String, String>();
	public String id;
	public String dateVerified;
	public String value;
	public String type;
	public String countryCode;
	
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDateVerified() {
		return dateVerified;
	}
	public void setDateVerified(String dateVerified) {
		this.dateVerified = dateVerified;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	
}
