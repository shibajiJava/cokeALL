package com.ibm.ko.cds.dao.metrics;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ibm.ko.cds.pojo.metrics.MonitorParams;
import com.ibm.ko.cds.pojo.metrics.SnapshotBean;

/*
 * Dao class for saving the data in the DB for the snap shot and for fetching the details of the mnitoring params
 */
@Component
public interface TimerDAO {
	
	public int postSnaphotInfo(SnapshotBean snapshotBean);
	
	public List<MonitorParams> getMonitoringParams();
	
}
