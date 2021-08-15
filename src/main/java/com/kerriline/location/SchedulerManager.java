package com.kerriline.location;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author Aleksey
 *
 */
public class SchedulerManager {

	private static final Logger LOG = LoggerFactory.getLogger(SchedulerManager.class);

	private String locationCronExpression;

	private String mileageCronExpression;

	@Autowired LocationManager location;
	@Autowired MileageManager mileage;

	private Scheduler scheduler;

    public SchedulerManager(String locationSchedule, String mileageSchedule) {
        locationCronExpression = locationSchedule;
        mileageCronExpression = mileageSchedule;
    }

    @PostConstruct
	public void setup() throws SchedulerException {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			scheduler = sf.getScheduler();
			locationTrigger();
			mileageTrigger();
			scheduler.start();
			LOG.info("Scheduler was setup successfully");
		} catch (Exception e) {
			LOG.error("Failed to setup scheduler", e);
		}
	}


	@PreDestroy
	public void close() throws SchedulerException {
		scheduler.shutdown();
	}

	private void locationTrigger() throws SchedulerException {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("LocationManager", location);

		JobDetail job = newJob(LocationScheduledJob.class)
				.usingJobData(newJobDataMap)
				.withIdentity("job1", "group1")
				.build();

		CronTrigger trigger = newTrigger()
				.withIdentity("trigger1", "group1")
				.withSchedule(cronSchedule(locationCronExpression))
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	private void mileageTrigger() throws SchedulerException {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put("MileageManager", mileage);

		JobDetail job = newJob(MileageScheduledJob.class)
				.usingJobData(newJobDataMap)
				.withIdentity("job2", "group2")
				.build();

		CronTrigger trigger = newTrigger()
				.withIdentity("trigger2", "group2")
				.withSchedule(cronSchedule(mileageCronExpression))
				.build();

		scheduler.scheduleJob(job, trigger);
	}
}
