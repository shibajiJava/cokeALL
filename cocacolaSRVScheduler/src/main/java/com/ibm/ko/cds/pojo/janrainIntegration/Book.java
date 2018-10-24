
package com.ibm.ko.cds.pojo.janrainIntegration;

import java.util.List;

public class Book{
   	private Number result_count;
   	private List results;
   	private String stat;

 	public Number getResult_count(){
		return this.result_count;
	}
	public void setResult_count(Number result_count){
		this.result_count = result_count;
	}
 	public List getResults(){
		return this.results;
	}
	public void setResults(List results){
		this.results = results;
	}
 	public String getStat(){
		return this.stat;
	}
	public void setStat(String stat){
		this.stat = stat;
	}
}
