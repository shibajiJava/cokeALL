package com.ko.cds.pojo.admin.answer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import net.sf.oval.constraint.NotNull;

public class GetAdminAnswer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private int optionId;
	
	@NotNull
	private String 	optionString;
	
	@NotNull	
	private int answerOrder;
	@NotNull
	private String isSelectedAsAnswer="false";
	
	private Date insertDate;
	
	private Date updateDate;

	public String getOptionString() {
		return optionString;
	}

	public void setOptionString(String optionString) {
		this.optionString = optionString;
	}
	public String getIsSelectedAsAnswer() {
		return isSelectedAsAnswer;
	}

	public void setIsSelectedAsAnswer(String isSelectedAsAnswer) {
		this.isSelectedAsAnswer = isSelectedAsAnswer;
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

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}



	
	
	
	
	
}
