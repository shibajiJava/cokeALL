package com.ko.cds.pojo.admin.question;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

public class UpdateAdminQuestion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Length(min=1)
	private BigInteger questionID;
	/**/
	private String questionType;
	/**/
	private String displayType;
	
	private BigInteger displayOrder;
	/**/
	private String questionString;
	/**/
	private Date insertDate;
	/**/
	private Date updateDate;

	@NotNull
	@Length(min=1)
	private BigInteger optionId;
	
	private String optionString;
	
	private int answerOrder;
	
	
	public BigInteger getOptionId() {
		return optionId;
	}
	public void setOptionId(BigInteger optionId) {
		this.optionId = optionId;
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
	

	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public String getQuestionString() {
		return questionString;
	}
	public void setQuestionString(String questionString) {
		this.questionString = questionString;
	}
	public BigInteger getQuestionID() {
		return questionID;
	}
	public void setQuestionID(BigInteger questionID) {
		this.questionID = questionID;
	}
	public BigInteger getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(BigInteger displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getOptionString() {
		return optionString;
	}
	public void setOptionString(String optionString) {
		this.optionString = optionString;
	}
	public int getAnswerOrder() {
		return answerOrder;
	}
	public void setAnswerOrder(int answerOrder) {
		this.answerOrder = answerOrder;
	}
}
