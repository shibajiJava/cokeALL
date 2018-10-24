package com.ko.cds.pojo.admin.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import net.sf.oval.constraint.Length;

public class QuestionIdAdmin implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

    @Length(min=1)
	private BigInteger surveyID;
		
	private Date insertDate;
	
	private Date updateDate;

	private BigInteger QuestionIds;
    


	public BigInteger getQuestionIds() {
		return QuestionIds;
	}

	public void setQuestionIds(BigInteger questionIds) {
		QuestionIds = questionIds;
	}

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

}
