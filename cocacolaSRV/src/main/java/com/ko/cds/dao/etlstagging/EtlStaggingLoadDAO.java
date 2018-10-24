package com.ko.cds.dao.etlstagging;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ko.cds.etlstagging.EtlLoadTracker;
import com.ko.cds.pojo.points.TransactionHistory;

@Component
public interface EtlStaggingLoadDAO {
	
	public List<TransactionHistory> getSACHistory();
	public List<TransactionHistory> getSavingStarHistory();
	public List<EtlLoadTracker> getETLLoadStatus();
	public void updateETLLoadStatus(EtlLoadTracker etlLoadTracker);
}
