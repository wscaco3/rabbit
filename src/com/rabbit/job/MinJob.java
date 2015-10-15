package com.rabbit.job;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
public class MinJob implements Job {
	private static Logger log = Logger.getLogger(MinJob.class.getName());

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	log.error("ok");
	}
    
}