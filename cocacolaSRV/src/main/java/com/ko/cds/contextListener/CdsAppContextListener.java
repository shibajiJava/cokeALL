package com.ko.cds.contextListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.jmxImpl.JmxConfigurationProperty;
import com.ko.cds.utils.PropFileMonitor;

/**
 * 
 * @author IBM This ContextListener will be responsible for App configuration
 *         file Listner
 */

public class CdsAppContextListener implements ServletContextListener {

	public static Logger logger = LoggerFactory
			.getLogger(CdsAppContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		try {

			Properties prop = new Properties();

			try {

				InputStream in = (CdsAppContextListener.class
						.getClassLoader()
						.getResourceAsStream(
								"resources/ConfigurationProp/configuration.properties"));

				prop.load(in);
				in.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
			logger.info("Loading oveal XML File at start up ");

			long pollingInterval =  Long.valueOf(prop.getProperty("pollingInterval"));
			String fileNames = prop.getProperty("validationFilePath");
			int index=0;
			for(String filename : fileNames.split(","))
			{
				URL url = CdsAppContextListener.class.getClassLoader().getResource(filename);
				JmxConfigurationProperty.ovalValidationFile[index++] = new File(url.getPath());
				PropFileMonitor.doMonitor(new File(url.getPath()), pollingInterval);
			}
			
			
			
			
			
			
		} catch(RuntimeException runExx)
		{
			//if any reason unable to load file or trimer application then also will run 
			JmxConfigurationProperty.ovalValidationFile[0] = new File(
					CdsAppContextListener.class
							.getClassLoader()
							.getResource("resources/validation/oval-config.xml")
							.getPath());
			logger.error(" Error in File Monitor start", runExx);
		}
	}

}
