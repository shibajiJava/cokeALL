package com.ko.cds.request.admin.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;



import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;



public class PostSurveyQuestionAdminRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurveyAdmin
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

    
    @NotNull
    @Length(min=1)
	private BigInteger surveyID;
    
    @NotNull
    private List<QuestionIdS> QuestionIds;
    
	
	private Date insertDate;
	
	private Date updateDate;

	public BigInteger getSurveyID() {
		return surveyID;
	}

	public void setSurveyID(BigInteger surveyID) {
		this.surveyID = surveyID;
	}

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
	@JsonProperty("QuestionIds")
	public List<QuestionIdS> getQuestionIds() {
		return QuestionIds;
	}

	public void setQuestionIds(List<QuestionIdS> questionIds) {
		QuestionIds = questionIds;
	}




    

	
	

}
