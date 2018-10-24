package com.ko.cds.request.admin.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;





import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Size;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.utils.CustomUTCDateSerializer;



public class PostSurveyAdminRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurveyAdmin
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	private BigInteger surveyId;
    @NotNull
    @Size(max=250)
	private String surveyDescription;
    @NotNull
	private String typeCode;
    @NotNull
	private String countryCode;
    @NotNull
	private String languageCode;
    @NotNull
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private Date expirationDate;
	@NotNull
	private String frequencyCode;
	@NotNull
	private String latestAnswerIndicator;
	/**
	 * new chages for Survey 
	 * 01/22/2016
	 */
	
	public BigInteger getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(BigInteger surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyDescription() {
		return surveyDescription;
	}
	public void setSurveyDescription(String surveyDescription) {
		this.surveyDescription = surveyDescription;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * @return the languageCode
	 */
	public String getLanguageCode() {
		return languageCode;
	}
	/**
	 * @param languageCode the languageCode to set
	 */
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	/**
	 * @return the frequencyCode
	 */
	public String getFrequencyCode() {
		return frequencyCode;
	}
	/**
	 * @param frequencyCode the frequencyCode to set
	 */
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}
	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getLatestAnswerIndicator() {
		return latestAnswerIndicator;
	}
	public void setLatestAnswerIndicator(String latestAnswerIndicator) {
		this.latestAnswerIndicator = latestAnswerIndicator;
	}
}
