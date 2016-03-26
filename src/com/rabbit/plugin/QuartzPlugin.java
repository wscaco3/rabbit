package com.rabbit.plugin;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
 
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
 
import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;
 
public class QuartzPlugin implements IPlugin {
 
    private final Log logger = Log.getLog(getClass());
     
    private static final String JOB = "job";
 
    private String config = "job.properties";
 
    private Properties properties;
 
    public QuartzPlugin(String config) {
        this.config = config;
    }
 
    public QuartzPlugin() {
    }
 
    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;
 
    public boolean start() {
        try {
            loadProperties();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false ;
        }
 
        if (properties == null) {
            return false;
        }
         
        schedulerFactory = new StdSchedulerFactory();
         
        try {
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            return false;
        }
 
        if (scheduler == null) {
            logger.error("scheduler is null");
            return false;
        }
 
        Enumeration<Object> enums = properties.keys();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement() + "";
            if (!key.endsWith(JOB) || !isTrue(getJobKey(key,"enable"))) {
                continue;
            }
             
            String jobClassName = properties.get(key) + "";
            String jobName  =     key.substring(0,key.lastIndexOf("."));
            String triggerName  =  "trigger_"+key.substring(0,key.lastIndexOf("."));
            String jobCronExp =   properties.getProperty(getJobKey(key,"cron")) + "";
            String jobGroup     = properties.getProperty(getJobKey(key,"group","jobGroup"));
             
            Class<? extends Job> jobClass = null;
            try {
                jobClass = (Class<? extends Job>) Class.forName(jobClassName);
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
                return false;
            }
             
            JobDetail job = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, jobGroup).build();
            CronTrigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerName, jobGroup)
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule(jobCronExp))
                    .build();
 
            try {
                scheduler.scheduleJob(job, trigger);
                scheduler.start();
            } catch (SchedulerException e) {
                logger.error(e.getMessage());
                return false;
            }
        }
         
        return true;
    }
 
    public boolean stop() {
 
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            return false;
        }
 
        return true;
    }
 
    private void loadProperties() throws IOException {
        properties = new Properties();
        InputStream is = QuartzPlugin.class.getClassLoader()
                .getResourceAsStream(config);
        properties.load(is);
    }
     
     
    private String getJobKey(String str,String type,String defaultValue) {
        String key = getJobKey(str,type);
         
        if (key == null || "".equals(key.trim()))
            return defaultValue;
         
        return key;
         
    }
     
    private String getJobKey(String str,String type) {
        return str.substring(0, str.lastIndexOf(JOB)) + type;
    }
     
    private boolean isTrue(String key) {
        Object enable = properties.get(key);
        if (enable != null && "false".equalsIgnoreCase((enable + "").trim())) {
            return false;
        }
        return true;
    }
 
}