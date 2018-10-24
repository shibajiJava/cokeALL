package com.ko.cds.response.admin.answer;

import java.io.Serializable;


import java.math.BigInteger;
import java.util.List;




public class GetAnswerAdminResponseList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private List<GetAnswerAdminResponse> answer;
	public List<GetAnswerAdminResponse> getAnswer() {
		return answer;
	}
	public void setAnswer(List<GetAnswerAdminResponse> answer) {
		this.answer = answer;
	}
	@Override
	public String toString() {
		return "GetAnswerAdminResponseList [answer=" + answer+"]";
	}
}
