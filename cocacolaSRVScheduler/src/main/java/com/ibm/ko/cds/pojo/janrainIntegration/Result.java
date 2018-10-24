
package com.ibm.ko.cds.pojo.janrainIntegration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

public class Result{
	private Map<String, String> other = new HashMap<String, String>();
   	private Number result_count;
   	private List<Results> results;
   	private String stat;

 	public Number getResult_count(){
		return this.result_count;
	}
	public void setResult_count(Number result_count){
		this.result_count = result_count;
	}
 	public List<Results> getResults(){
		return this.results;
	}
	public void setResults(List<Results> results){
		this.results = results;
	}
 	public String getStat(){
		return this.stat;
	}
	public void setStat(String stat){
		this.stat = stat;
	}
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
}
