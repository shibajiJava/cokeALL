package com.ko.cds.pojo.admin.site;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;




public class UpdateAdminSite implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @NotNull
    @Length(min=1)
	private BigInteger siteId;
	
	private String siteName;
	
	private Date insertDate;
	
	private Date updateDate;

	
	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

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
