package com.ko.cds.TimerTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ko.cds.utils.CDSOUtils;

public class JanrainTimerTaskForOpts extends TimerTask {
	ExecutorService service = Executors.newFixedThreadPool(500);
	@Override
	public void run() {
		
		for (int i =0; i<CDSOUtils.JanrainOptsTaskList.size(); i++){
				//System.out.println("Processing : "+i);
	           service.submit(CDSOUtils.JanrainOptsTaskList.get(i));
	           CDSOUtils.JanrainOptsTaskList.remove(i);
	       }
		//service.shutdown();
		
	}

}
