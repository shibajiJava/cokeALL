package com.ko.cds.request.csr;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import com.ko.cds.pojo.activity.SearchActivityTagObject;

/**
 * Pojo for Capturing Request for Activity Search Request
 * @author ibm
 *
 */
public class ActivitySearchRequest implements Serializable{
	
	private static final long serialVersionUID = 190909L;

	@QueryParam("sessionUUID")
	@MaxLength(value = 36)
	private String sessionUUID;
	
	@NotNull
	@QueryParam("memberId")
	@Length(min = 1)
	private BigInteger	memberId;
	
	private SearchActivityTagObject tagObject;
	
	@QueryParam("startDate")
	private String 	startDate;
	
	
	@QueryParam("endDate")
	private String 	endDate;
	
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
	public SearchActivityTagObject getTagObject() {
		return tagObject;
	}
	public void setTagObject(SearchActivityTagObject tagObject) {
		this.tagObject = tagObject;
	}
	public String getStartDate() {
		if(startDate != null && startDate.contains("T")){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");		
			try {
				Date date1 = format.parse(this.startDate);
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String outputDate = format.format(date1).toString();
				this.startDate = outputDate;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		if(endDate != null && endDate.contains("T")){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");		
			try {
				Date date1 = format.parse(this.endDate);
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String outputDate = format.format(date1).toString();
				this.endDate = outputDate;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "ActivitySearchRequest [sessionUUID=" + sessionUUID
				+ ", memberId=" + memberId + ", tagObject=" + tagObject
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
	


}
