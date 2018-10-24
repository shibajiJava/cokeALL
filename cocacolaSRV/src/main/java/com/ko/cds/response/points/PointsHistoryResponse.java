package com.ko.cds.response.points;

import java.io.Serializable;
import java.util.List;

import com.ko.cds.pojo.points.PointsHistory;

/**
 * Response class for points history.
 * 
 * @author IBM
 * 
 */
public class PointsHistoryResponse implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private List<PointsHistory> transaction;

	private String maximumRecords;
	
	public String getMaximumRecords() {
		return maximumRecords;
	}

	public void setMaximumRecords(String maximumRecords) {
		this.maximumRecords = maximumRecords;
	}

	public List<PointsHistory> getTransaction() {
		return transaction;
	}

	public void setTransaction(List<PointsHistory> transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "PointsHistoryResponse [transaction=" + transaction
				+ ", maximumRecords=" + maximumRecords + "]";
	}

}
