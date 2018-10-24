package com.ko.cds.pojo.admin.question;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

public class AnswerOptionslistAdminObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Length(min=1)
	private BigInteger optionId;
	/**/
	private String optionString;
	/**/
	private String isSelectedAnswer = "false";

	private String optionType;
	
	/**/
	private Date insertDate;
	/**/
	private Date updateDate;
	
	private int answerOrder;
	
	private int desiredanswer;
	
	
	public int getDesiredanswer() {
		return desiredanswer;
	}
	public void setDesiredanswer(int desiredanswer) {
		this.desiredanswer = desiredanswer;
	}
	public BigInteger getOptionId() {
		return optionId;
	}
	public void setOptionId(BigInteger optionId) {
		this.optionId = optionId;
	}
	public String getOptionString() {
		return optionString;
	}
	public void setOptionString(String optionString) {
		this.optionString = optionString;
	}
	public String getIsSelectedAnswer() {
		return isSelectedAnswer;
	}
	public void setIsSelectedAnswer(String isSelectedAnswer) {
		this.isSelectedAnswer = isSelectedAnswer;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	public int getAnswerOrder() {
		return answerOrder;
	}
	public void setAnswerOrder(int answerOrder) {
		this.answerOrder = answerOrder;
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
