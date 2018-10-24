package com.ko.cds.TimerTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ko.cds.utils.CDSOUtils;

public class JanrainTimerTask extends TimerTask{
	ExecutorService service = Executors.newFixedThreadPool(500);
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		for (int i =0; i<CDSOUtils.metricsLoggerTaskList.size(); i++){
				//System.out.println("Processing : "+i);
	           service.submit(CDSOUtils.metricsLoggerTaskList.get(i));
	           CDSOUtils.metricsLoggerTaskList.remove(i);
	       }
		
	}
}
