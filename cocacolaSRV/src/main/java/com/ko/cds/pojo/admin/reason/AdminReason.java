package com.ko.cds.pojo.admin.reason;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.Size;

import net.sf.oval.constraint.NotNull;

public class AdminReason implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size(max=50)
	private String 	reasonCode;
	
	@NotNull
	@Size(max=50)
	private String 	reasonCodeDescirption;
	

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

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonCodeDescirption() {
		return reasonCodeDescirption;
	}

	public void setReasonCodeDescirption(String reasonCodeDescirption) {
		this.reasonCodeDescirption = reasonCodeDescirption;
	}
	
	
	
	
	
}
