
package com.ko.cds.pojo.janrainIntegration;


public class Organizations{
   	private String department;
   	private String description;
   	private String endDate;
   	private Number id;
   	private Location location;
   	private String name;
   	private String primary;
   	private String startDate;
   	private String title;
   	private String type;

 	public String getDepartment(){
		return this.department;
	}
	public void setDepartment(String department){
		this.department = department;
	}
 	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
 	public String getEndDate(){
		return this.endDate;
	}
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}
 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
 	public Location getLocation(){
		return this.location;
	}
	public void setLocation(Location location){
		this.location = location;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public String getPrimary(){
		return this.primary;
	}
	public void setPrimary(String primary){
		this.primary = primary;
	}
 	public String getStartDate(){
		return this.startDate;
	}
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
