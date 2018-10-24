package com.ko.cds.TimerTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ko.cds.utils.CDSOUtils;

public class BunchBallActivityTask extends TimerTask {
	ExecutorService service = Executors.newFixedThreadPool(1000);
	@Override
	public void run() {
		//System.out.println("Activity Size :" + CDSOUtils.BunchBallActivityAsynList.size());
		for (int i =0; i<CDSOUtils.BunchBallActivityAsynList.size(); i++){
				//System.out.println("Processing : "+i);
	           service.submit(CDSOUtils.BunchBallActivityAsynList.get(i));
	           CDSOUtils.BunchBallActivityAsynList.remove(i);
	       }
		
		
	}

}
