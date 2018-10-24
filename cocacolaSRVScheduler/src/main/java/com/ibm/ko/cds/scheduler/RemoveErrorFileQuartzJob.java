package com.ibm.ko.cds.scheduler;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.ibm.ko.cds.util.MetricsSchUtil;


/**
 * A custom QuartzJob job that extends from the QuartzJobBean
 * By default, Quartz Jobs are stateless, implement the Stateful interface,
 * The second job will not start before the first one has finished if two triggers are pointed to same job.
 *
 */
@Component
public class RemoveErrorFileQuartzJob extends QuartzJobBean implements StatefulJob{

	private MetricsSchHelper metricsSchHelper;
	
	private static final Logger logger = LoggerFactory
			.getLogger(RemoveErrorFileQuartzJob.class);
		/**
	 * @return the metricsSchHelper
	 */
	public MetricsSchHelper getMetricsSchHelper() {
		return metricsSchHelper;
	}


	/**
	 * @param metricsSchHelper the metricsSchHelper to set
	 */
	public void setMetricsSchHelper(MetricsSchHelper metricsSchHelper) {
		this.metricsSchHelper = metricsSchHelper;
	}
	@Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		   // Do the actual work
			metricsSchHelper.deleteErrorFiles();
		   // Retrieve the next date when the job will be run
			logger.info("Next Run time: ================= " + ctx.getNextFireTime()+"=====================");
	}
}
