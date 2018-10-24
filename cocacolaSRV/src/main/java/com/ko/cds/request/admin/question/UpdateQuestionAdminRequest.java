package com.ko.cds.request.admin.question;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;



import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotNull;




public class UpdateQuestionAdminRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurvey
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private BigInteger questionID;

	@NotNull
	private String questionString;
	
	@NotNull
	private String displayType;

	@NotNull
	private BigInteger displayOrder;
	
	@NotNull
	private String questionType;
	
	private Date insertDate;
	
	private Date updateDate;
	
	@NotNull
	private List<answerIdsList> answerIds ;
	


	public BigInteger getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(BigInteger displayOrder) {
		this.displayOrder = displayOrder;
	}
	public List<answerIdsList> getAnswerIds() {
		return answerIds;
	}
	public void setAnswerIds(List<answerIdsList> answerIds) {
		this.answerIds = answerIds;
	}

	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
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
	public String getQuestionString() {
		return questionString;
	}
	public void setQuestionString(String questionString) {
		this.questionString = questionString;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public BigInteger getQuestionID() {
		return questionID;
	}
	public void setQuestionID(BigInteger questionID) {
		this.questionID = questionID;
	}
}
