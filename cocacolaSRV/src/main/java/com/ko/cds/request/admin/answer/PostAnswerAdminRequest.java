package com.ko.cds.request.admin.answer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import com.ko.cds.pojo.question.AnswerOptionsObject;
import com.ko.cds.pojo.question.Question;



public class PostAnswerAdminRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurvey
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private BigInteger answerID;
	
	@NotNull
	private String 	optionString;
	

	@NotNull	
	private BigInteger answerOrder;
 
	
	private Date insertDate;
	
	private Date updateDate;



	public String getOptionString() {
		return optionString;
	}

	public void setOptionString(String optionString) {
		this.optionString = optionString;
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

	public BigInteger getAnswerOrder() {
		return answerOrder;
	}

	public void setAnswerOrder(BigInteger answerOrder) {
		this.answerOrder = answerOrder;
	}

	public BigInteger getAnswerID() {
		return answerID;
	}

	public void setAnswerID(BigInteger answerID) {
		this.answerID = answerID;
	}



}
