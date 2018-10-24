package com.ko.cds.response.admin.question;

import java.io.Serializable;


import java.math.BigInteger;
import java.util.List;




public class GetQuestionAdminResponseList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private List<GetQuestionAdminResponse> questions;
	public List<GetQuestionAdminResponse> getQuestions() {
		return questions;
	}
	public void setQuestions(List<GetQuestionAdminResponse> questions) {
		this.questions = questions;
	}
	
	@Override
	public String toString() {
		return "GetQuestionAdminResponseList [questions=" + questions+"]";
	}
}
