package com.rabbit.job;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.log.Log;
public class MinJob implements Job {
    private final Log log = Log.getLog(getClass());
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	log.error("ok");
	}
    
}