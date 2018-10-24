package com.ko.cds.TimerTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ko.cds.utils.CDSOUtils;

public class JanrainTimerTaskForMemberUpdate extends TimerTask {
	ExecutorService service = Executors.newFixedThreadPool(500);
	@Override
	public void run() {
		
		for (int i =0; i<CDSOUtils.JanrainMemberUpdateTaskList.size(); i++){
				//System.out.println("Processing : "+i);
	           service.submit(CDSOUtils.JanrainMemberUpdateTaskList.get(i));
	           CDSOUtils.JanrainMemberUpdateTaskList.remove(i);
	       }
		//service.shutdown();
	}
		
	}


