
package com.ibm.ko.cds.pojo.janrainIntegration;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;


public class ExternalIds{
	private Map<String, String> other = new HashMap<String, String>();
   	private Number id;
   	private String lastUpdatedTime;
   	private String type;
   	private boolean useForLogin;
   	private String value;
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
 	public String getLastUpdatedTime(){
		return this.lastUpdatedTime;
	}
	public void setLastUpdatedTime(String lastUpdatedTime){
		this.lastUpdatedTime = lastUpdatedTime;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
 	public boolean getUseForLogin(){
		return this.useForLogin;
	}
	public void setUseForLogin(boolean useForLogin){
		this.useForLogin = useForLogin;
	}
 	public String getValue(){
		return this.value;
	}
	public void setValue(String value){
		this.value = value;
	}
}
