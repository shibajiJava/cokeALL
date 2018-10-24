package com.ko.cds.pojo.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import net.sf.oval.constraint.MaxLength;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.jvnet.hk2.annotations.Optional;

import com.ko.cds.utils.CustomUTCDateSerializer;
//import java.util.Date;

public class Survey implements Serializable{

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
	private String expirationDate;
	
	private String frequencyCode;
	
	
	private String latestAnswerIndicator;
	private List<Question> questions;
	
	
	
	
//	private Date insertDate;
//	private Date updateDate;
	
	
	

//	public Date getInsertDate() {
//		return insertDate;
//	}
//
//	public void setInsertDate(Date insertDate) {
//		this.insertDate = insertDate;
//	}
//
//	public Date getUpdateDate() {
//		return updateDate;
//	}
//
//	public void setUpdateDate(Date updateDate) {
//		this.updateDate = updateDate;
//	}


	

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
	
	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
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

	public String getLatestAnswerIndicator() {
		return latestAnswerIndicator;
	}

	public void setLatestAnswerIndicator(String latestAnswerIndicator) {
		this.latestAnswerIndicator = latestAnswerIndicator;
	}


	public List<Question> getQuestions() {
		
		
		
		
		
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	

	
	
	

}
