package com.ko.cds.response.admin.answer;

import java.io.Serializable;


import java.math.BigInteger;


public class PostAnswerAdminResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private BigInteger answerID;
	public BigInteger getAnswerID() {
		return answerID;
	}
	public void setAnswerID(BigInteger answerID) {
		this.answerID = answerID;
	}



	
	
	
}
