package com.ibm.ko.cds.services.appcontext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 
 * @author IBM
 *
 */

public class ApplicationContextProvider implements ApplicationContextAware{

	private static ApplicationContext ctx = null;
	 public static ApplicationContext getApplicationContext() {
	return ctx;
	 }
	 public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		 this.ctx = ctx;
	 }
}
