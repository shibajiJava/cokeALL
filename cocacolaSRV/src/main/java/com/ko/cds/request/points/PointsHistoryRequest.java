package com.ko.cds.request.points;

import java.io.Serializable;
import java.math.BigInteger;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

/**
 * Request class for points history.
 * 
 * @author IBM
 * 
 */
public class PointsHistoryRequest implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	@MaxLength(value = 36)
	@QueryParam("sessionUUID")
	private String sessionUUID;
	@NotNull
	@QueryParam("memberId")
	private BigInteger memberId;
	@NotNull
	@MaxLength(value = 10)
	@QueryParam("accountType")
	private String accountType;
	@NotNull
	@QueryParam("startDateTime")
	private String startDateTime;
	@QueryParam("endDateTime")
	@NotNull
	private String endDateTime;
	@QueryParam("ascending")
	private boolean ascending;
	@QueryParam("offset")
	private Integer offset;
	@QueryParam("limit")
	private Integer limit;
	@MaxLength(value = 20)
	@QueryParam("reasonCode")
	private String reasonCode;

	public String getSessionUUID() {
		return sessionUUID;
	}

	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public boolean getAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

}
