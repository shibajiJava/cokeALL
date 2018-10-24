package com.ko.cds.request.admin.answer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

public class answerIdsList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Length(min=1)
	private BigInteger optionId;

	

	public BigInteger getOptionId() {
		return optionId;
	}
	public void setOptionId(BigInteger optionId) {
		this.optionId = optionId;
	}
	

}
