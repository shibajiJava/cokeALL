package com.ko.cds.response.admin.site;

import java.io.Serializable;


import java.math.BigInteger;


public class GetSiteAdminResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private int siteId;
	
	private String siteName;
	
	

	public int getSiteId() {
		return siteId;
	}



	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}



	public String getSiteName() {
		return siteName;
	}



	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}



	@Override
	public String toString() {
		return "GetSiteAdminResponse [siteId=" +siteId+", siteName"+siteName+"]";
	}

	
	
	
}
