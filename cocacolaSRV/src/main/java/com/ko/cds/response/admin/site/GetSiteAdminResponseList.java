package com.ko.cds.response.admin.site;

import java.io.Serializable;


import java.math.BigInteger;
import java.util.List;




public class GetSiteAdminResponseList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private List<GetSiteAdminResponse> sites;
	
	public List<GetSiteAdminResponse> getSites() {
		return sites;
	}

	public void setSites(List<GetSiteAdminResponse> sites) {
		this.sites = sites;
	}

	@Override
	public String toString() {
		return "GetSiteAdminResponseList [sites=" + sites+"]";
	}
}
