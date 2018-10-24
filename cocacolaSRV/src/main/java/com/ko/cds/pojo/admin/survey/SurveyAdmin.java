package com.ko.cds.pojo.admin.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.utils.CustomUTCDateSerializer;
//import java.util.Date;

public class SurveyAdmin implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private BigInteger surveyId;
	
	private String surveyDescription;
	
	private String typeCode;
	
	private String countryCode;
	
	private String languageCode;
	
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private Date expirationDate;
	
	private String frequencyCode;
	

	public String getSurveyDescription() {
		return surveyDescription;
	}

	public BigInteger getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(BigInteger surveyId) {
		this.surveyId = surveyId;
	}

	public void setSurveyDescription(String surveyDescription) {
		this.surveyDescription = surveyDescription;
	}

	

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getFrequencyCode() {
		return frequencyCode;
	}

	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}
