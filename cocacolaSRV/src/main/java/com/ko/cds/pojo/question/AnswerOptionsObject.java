package com.ko.cds.pojo.question;

import java.io.Serializable;
import java.math.BigInteger;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

public class AnswerOptionsObject implements Serializable{

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
	private String isSelectedAnswer = "false";;
	
	
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
	

}
