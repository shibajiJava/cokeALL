package com.ko.cds.service.helper;

import org.springframework.context.ApplicationContext;

import com.ibm.app.services.appcontext.ApplicationContextProvider;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.activity.BunchBallActivityRequest;

public class BunchBallActivityAsynService  implements Runnable {

	private BunchBallActivityRequest bunchBallActivityRequest;
	ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
	private ActivityServiceHelper serviceHelper = (ActivityServiceHelper) ctx.getBean("activityServiceHelper",ActivityServiceHelper.class);
	
	public BunchBallActivityAsynService()
	{
		
	}
	
	public BunchBallActivityAsynService(BunchBallActivityRequest bunchBallActivityRequest)
	{
		this.bunchBallActivityRequest=bunchBallActivityRequest;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			bunchBallActivityAsyncUpdate(this.bunchBallActivityRequest);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void  bunchBallActivityAsyncUpdate(BunchBallActivityRequest bunchBallActivityRequest) throws BadRequestException
	{
		serviceHelper.bunchBallActivity(bunchBallActivityRequest);
	}
}
