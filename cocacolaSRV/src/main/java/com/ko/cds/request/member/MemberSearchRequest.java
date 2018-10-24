package com.ko.cds.request.member;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import com.ko.cds.utils.DictionaryValue;

/**
 * 
 * @author IBM
 *
 */

public class MemberSearchRequest {
	@QueryParam("sessionUUID")
	/** The unique session ID */
	@MaxLength(value=36)
	private String sessionUUID;
	@QueryParam("searchParameterName")
	/** The name of the field that will be searched**/
	
	
	@NotNull(message="member.search.violation")
	//@ValidateWithMethod(parameterType=String.class,methodName="isvalidateSearchParameterName", message="Search pamameter value is not valid")
	//@DictionaryValue(file="resources/validation/searchMemberParameters.txt")
	@MaxLength(value=32, message="member.search.violation")
	private String searchParameterName;
	@QueryParam("searchParameterValue")
	/** The value of the field that will be searched. Maximum length 250 * **/
	@NotNull
	@MaxLength(value=250)
	private String searchParameterValue;
	@QueryParam("memberStatus")
	/**The member status that exists example: active, inactive. It is optional*/
	@MaxLength(value=10, message="member.search.violation")
	@DictionaryValue(file="resources/validation/searchMemberStatus.txt")
	private String memberStatus;
	
	public String getSessionUUID() {
		return sessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}
	public String getSearchParameterName() {
		return searchParameterName;
	}
	public void setSearchParameterName(String searchParameterName) {
		this.searchParameterName = searchParameterName;
	}
	public String getSearchParameterValue() {
		return searchParameterValue;
	}
	public void setSearchParameterValue(String searchParameterValue) {
		this.searchParameterValue = searchParameterValue;
	}
	public String getMemberStatus() {
		return memberStatus;
	}
	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}
	
	private boolean isvalidateSearchParameterName(String searchParameterName){
		if(searchParameterName!= null && ("Vending ID".equals(searchParameterName) || "SMS Number".equals(searchParameterName) || "CDS Member ID".equals(searchParameterName)||"Email".equals(searchParameterName) || "Janrain UUID".equals(searchParameterName))){
			return true;
		}else{
			return false;
		}
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MemberSearchRequest [sessionUUID=" + sessionUUID
				+ ", searchParameterName=" + searchParameterName
				+ ", searchParameterValue=" + searchParameterValue
				+ ", memberStatus=" + memberStatus + "]";
	}
	
}
