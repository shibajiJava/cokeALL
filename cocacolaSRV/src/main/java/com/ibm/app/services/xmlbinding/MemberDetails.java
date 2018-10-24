package com.ibm.app.services.xmlbinding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "phnumber",

})
@XmlRootElement(name = "MemberDetails")
public class MemberDetails {
	@XmlElement(name = "Name")
    protected String name;
	@XmlElement(name = "Phnumber")
	protected String phnumber;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhnumber() {
		return phnumber;
	}
	public void setPhnumber(String phnumber) {
		this.phnumber = phnumber;
	}
	@Override
	public String toString() {
		return name;
		
	}
}
