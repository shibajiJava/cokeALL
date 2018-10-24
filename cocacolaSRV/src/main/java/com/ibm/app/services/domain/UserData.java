package com.ibm.app.services.domain;

import java.io.Serializable;

import org.springframework.stereotype.Component;


public class UserData implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String empname;
	private String address;
	private String empid; 
	
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	
	@Override
	public String toString() {
		        return "UserData [empname=" + empname + ", address=" + address + ", empid=" + empid+ "]";
		    }
	
	
}
