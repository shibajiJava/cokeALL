package com.ko.cds.response.admin.site;

import java.io.Serializable;


import java.math.BigInteger;


public class PostSiteAdminResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private BigInteger siteId;
	public BigInteger getSiteId() {
		return siteId;
	}
	public void setSiteId(BigInteger siteId) {
		this.siteId = siteId;
	}
	


	
	
	
}
