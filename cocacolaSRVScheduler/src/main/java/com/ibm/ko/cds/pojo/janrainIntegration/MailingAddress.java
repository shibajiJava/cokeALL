
package com.ibm.ko.cds.pojo.janrainIntegration;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;


public class MailingAddress{
	private Map<String, String> other = new HashMap<String, String>();
   	private String administrativeArea;
   	private String country;
   	private String municipality;
   	private String postalCode;
   	private String streetName1;
   	private String streetName2;
   	private String subAdministrativeArea;
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
	
 	public String getAdministrativeArea(){
		return this.administrativeArea;
	}
	public void setAdministrativeArea(String administrativeArea){
		this.administrativeArea = administrativeArea;
	}
 	public String getCountry(){
		return this.country;
	}
	public void setCountry(String country){
		this.country = country;
	}
 	public String getMunicipality(){
		return this.municipality;
	}
	public void setMunicipality(String municipality){
		this.municipality = municipality;
	}
 	public String getPostalCode(){
		return this.postalCode;
	}
	public void setPostalCode(String postalCode){
		this.postalCode = postalCode;
	}
 	public String getStreetName1(){
		return this.streetName1;
	}
	public void setStreetName1(String streetName1){
		this.streetName1 = streetName1;
	}
 	public String getStreetName2(){
		return this.streetName2;
	}
	public void setStreetName2(String streetName2){
		this.streetName2 = streetName2;
	}
 	public String getSubAdministrativeArea(){
		return this.subAdministrativeArea;
	}
	public void setSubAdministrativeArea(String subAdministrativeArea){
		this.subAdministrativeArea = subAdministrativeArea;
	}
}
