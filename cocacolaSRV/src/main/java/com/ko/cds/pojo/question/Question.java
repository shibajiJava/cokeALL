package com.ko.cds.pojo.question;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

public class Question implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Length(min=1)
	private BigInteger questionId;
	/**/
	private String questionType;
	/**/
	private String displayType;
	
	private int displayOrder;
	/**/
	private String questionText;
	/**/
	private int minAnswers;
	/**/
	private int maxAnswers;
	/**/
	private Date insertDate;
	/**/
	private Date updateDate;
	@AssertValid
	private List<AnswerOptionsObject> answerOptions ;
	
	
	
	
	public List<AnswerOptionsObject> getAnswerOptions() {
		return answerOptions;
	}
	public void setAnswerOptions(List<AnswerOptionsObject> answerOptions) {
		this.answerOptions = answerOptions;
	}
	public String getQuestionText() {
		return questionText;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
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
	
	public BigInteger getQuestionId() {
		return questionId;
	}
	public void setQuestionId(BigInteger questionId) {
		this.questionId = questionId;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public int getMinAnswers() {
		return minAnswers;
	}
	public void setMinAnswers(int minAnswers) {
		this.minAnswers = minAnswers;
	}
	public int getMaxAnswers() {
		return maxAnswers;
	}
	public void setMaxAnswers(int maxAnswers) {
		this.maxAnswers = maxAnswers;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	
}
