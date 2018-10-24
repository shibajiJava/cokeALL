package com.ibm.ko.cds.pojo.janrainIntegration;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;



public class CdsoProfileInterest implements Serializable {

	/**
	 * 
	 */
	
	private BigInteger  prifileInterestId;
	private BigInteger profileId;
	private String interestName;
	private BigInteger memberId;
	private String status; 
	private BigInteger janrainInterestId;
	private List<CdsoProfileInterestDtl> profileInterestDtl;
	
	
	public List<CdsoProfileInterestDtl> getProfileInterestDtl() {
		return profileInterestDtl;
	}
	public void setProfileInterestDtl(
			List<CdsoProfileInterestDtl> profileInterestDtl) {
		this.profileInterestDtl = profileInterestDtl;
	}
	private Timestamp insertDtm;
	private Timestamp updateDtm;
	
	public BigInteger getPrifileInterestId() {
		return prifileInterestId;
	}
	public void setPrifileInterestId(BigInteger prifileInterestId) {
		this.prifileInterestId = prifileInterestId;
	}
	public BigInteger getProfileId() {
		return profileId;
	}
	public void setProfileId(BigInteger profileId) {
		this.profileId = profileId;
	}
	public String getInterestName() {
		return interestName;
	}
	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getInsertDtm() {
		return insertDtm;
	}
	public void setInsertDtm(Timestamp insertDtm) {
		this.insertDtm = insertDtm;
	}
	public Timestamp getUpdateDtm() {
		return updateDtm;
	}
	public void setUpdateDtm(Timestamp updateDtm) {
		this.updateDtm = updateDtm;
	}
	public BigInteger getJanrainInterestId() {
		return janrainInterestId;
	}
	public void setJanrainInterestId(BigInteger janrainInterestId) {
		this.janrainInterestId = janrainInterestId;
	}
	
	
	
	
	
	
	
}
