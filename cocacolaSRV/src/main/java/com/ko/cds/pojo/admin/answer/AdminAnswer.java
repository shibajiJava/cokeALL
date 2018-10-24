package com.ko.cds.pojo.admin.answer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import net.sf.oval.constraint.NotNull;

public class AdminAnswer implements Serializable{

	/**
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

	public BigInteger getAnswerID() {
		return answerID;
	}

	public void setAnswerID(BigInteger answerID) {
		this.answerID = answerID;
	}

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


	
	
	
	
	
}
