package com.ko.cds.request.admin.site;

import java.io.Serializable;
import java.math.BigInteger;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;




public class UpdateSiteAdminRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurvey
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private BigInteger siteId;
	@NotNull
	private String siteName;


	public BigInteger getSiteId() {
		return siteId;
	}


	public void setSiteId(BigInteger siteId) {
		this.siteId = siteId;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	
	
	
}
