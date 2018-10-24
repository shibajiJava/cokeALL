package com.ibm.app.services.xmlbinding;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "phnumber",

})
@XmlRootElement(name = "MemberInfo")
public class MemberInfo {

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
