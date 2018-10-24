package com.ko.cds.response.admin.answer;

import java.io.Serializable;


import java.math.BigInteger;


public class GetAnswerAdminResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private int optionId;
	
	private String optionString;
	
	/*private String isSelectedAsAnswer;*/
	
	private int answerOrder;  

	public String getOptionString() {
		return optionString;
	}
	public void setOptionString(String optionString) {
		this.optionString = optionString;
	}
/*	public String getIsSelectedAsAnswer() {
		return isSelectedAsAnswer;
	}
	public void setIsSelectedAsAnswer(String isSelectedAsAnswer) {
		this.isSelectedAsAnswer = isSelectedAsAnswer;
	}*/
	public int getAnswerOrder() {
		return answerOrder;
	}
	public void setAnswerOrder(int answerOrder) {
		this.answerOrder = answerOrder;
	}
	public int getOptionId() {
		return optionId;
	}
	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	@Override
	public String toString() {
		return "GetAnswerAdminResponse [optionId=" +optionId+", optionString"+optionString+"answerOrder"+answerOrder+"]";
	}

	
	
	
}
