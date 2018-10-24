package com.ko.cds.TimerTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ko.cds.utils.CDSOUtils;

public class JanrainTimerTaskForPostOpts extends TimerTask {
	ExecutorService service = Executors.newFixedThreadPool(500);
	@Override
	public void run() {
		
		for (int i =0; i<CDSOUtils.JanrainPostOptsTaskList.size(); i++){
				//System.out.println("Processing : "+i);
	           service.submit(CDSOUtils.JanrainPostOptsTaskList.get(i));
	           CDSOUtils.JanrainPostOptsTaskList.remove(i);
	       }
		//service.shutdown();
		
	}

}
