package com.ko.cds.report.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
public class MetricsListener extends MetricsServlet.ContextListener {
	//private static final Logger logger = LoggerFactory.getLogger(MetricsListener.class);
	
    public static final MetricRegistry REGISTRY = new MetricRegistry();
    @Override
    protected MetricRegistry getMetricRegistry() {
    	//logger.info("Metrics Registry initialised...");
    	return REGISTRY;
    }
}