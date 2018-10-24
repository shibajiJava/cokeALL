package com.ko.cds.response.admin.question;

import java.io.Serializable;


import java.math.BigInteger;


public class PostQuestionAdminResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private BigInteger questionID;
	public BigInteger getQuestionID() {
		return questionID;
	}
	public void setQuestionID(BigInteger questionID) {
		this.questionID = questionID;
	}


	
	
	
}
