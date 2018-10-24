
package com.ko.cds.pojo.janrainIntegration;


public class Name{
   	private String familyName;
   	private String formatted;
   	private String givenName;
   	private String honorificPrefix;
   	private String honorificSuffix;
   	private String middleName;

 	public String getFamilyName(){
		return this.familyName;
	}
	public void setFamilyName(String familyName){
		this.familyName = familyName;
	}
 	public String getFormatted(){
		return this.formatted;
	}
	public void setFormatted(String formatted){
		this.formatted = formatted;
	}
 	public String getGivenName(){
		return this.givenName;
	}
	public void setGivenName(String givenName){
		this.givenName = givenName;
	}
 	public String getHonorificPrefix(){
		return this.honorificPrefix;
	}
	public void setHonorificPrefix(String honorificPrefix){
		this.honorificPrefix = honorificPrefix;
	}
 	public String getHonorificSuffix(){
		return this.honorificSuffix;
	}
	public void setHonorificSuffix(String honorificSuffix){
		this.honorificSuffix = honorificSuffix;
	}
 	public String getMiddleName(){
		return this.middleName;
	}
	public void setMiddleName(String middleName){
		this.middleName = middleName;
	}
}
