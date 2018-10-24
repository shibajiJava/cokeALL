package com.ko.cds.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.jmxImpl.JmxConfigurationProperty;

public class PropFileMonitor {

	static String FILE_PATH;
	static long pollingInterval = 0;
	static File validationFile;
	static File folder = null;
	static URL url=null;
	public static Logger logger = LoggerFactory.getLogger(CDSOUtils.class);

	public static void doMonitor(File validationFileObj, long pollingInterval) {

		Properties prop = new Properties();
		logger.info("=============== PropFileMonitor =======");
		
		logger.info("pollingInterval : " + pollingInterval);
		validationFile = validationFileObj;

		logger.info("File Path to monitor : " + validationFileObj.getName());

		folder=validationFileObj.getParentFile();

		if (!validationFile.exists()) {
			// Test to see if monitored folder exists
			throw new RuntimeException("Directory not found: " + validationFileObj.getPath());
		}
		logger.info("Folder Path to monitor : " + folder.getName());
		FileAlterationObserver observer = new FileAlterationObserver(folder);
		FileAlterationMonitor monitor = new FileAlterationMonitor(
				pollingInterval);

		FileAlterationListenerAdaptor listener = new FileAlterationListenerAdaptor() {
			// Is triggered when a file is created in the monitored folder

			@Override
			public void onFileChange(final File file) {
				// "file" is the reference to the changed file
				logger.info("File chnage detected");
				int index=0;
				for(File validationFile : JmxConfigurationProperty.ovalValidationFile)
				{
					if(validationFile!=null && file!=null)
					{
						if(validationFile.getName().equals(file.getName()))
						{
							JmxConfigurationProperty.ovalValidationFile[index++] = file;
							logger.info("loading new file in JMX configuration at index "+index+" for File "+validationFile.getName());
						}
					}
				}
				
				
				
				
			}

		};

		
		observer.addListener(listener);
		monitor.addObserver(observer);
		try {
			monitor.start();
		} catch (Exception exx) {

			logger.error("Error in start monitor thread ", exx);

		}
	}

}
