
package com.ibm.ko.cds.pojo.janrainIntegration;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;


public class Emails{
	private Map<String, String> other = new HashMap<String, String>();
   	private Number id;
   	private String primary;
   	private String type;
   	private String value;
   	private String schedulePreference;
   	private String format;
   	private String dateVerified;
   	private String target;
   	private String valid;
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
 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
 	public String getPrimary(){
		return this.primary;
	}
	public void setPrimary(String primary){
		this.primary = primary;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
 	public String getValue(){
		return this.value;
	}
	public void setValue(String value){
		this.value = value;
	}
	
	public String getSchedulePreference() {
		return schedulePreference;
	}
	public void setSchedulePreference(String schedulePreference) {
		this.schedulePreference = schedulePreference;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getDateVerified() {
		return dateVerified;
	}
	public void setDateVerified(String dateVerified) {
		this.dateVerified = dateVerified;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}

}
