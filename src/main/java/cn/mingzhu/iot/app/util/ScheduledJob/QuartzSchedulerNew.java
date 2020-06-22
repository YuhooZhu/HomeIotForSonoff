package cn.mingzhu.iot.app.util.ScheduledJob;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import cn.mingzhu.iot.app.common.iot.deviceQuartz.DeviceQuartzRow;
import cn.mingzhu.iot.app.common.iot.deviceQuartz.DeviceQuartzService;
import cn.mingzhu.iot.app.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Configuration
@Slf4j
public class QuartzSchedulerNew {
	// @Autowired
	// private MailJobMapper mailJobMapper;
	// 任务调度
	@Autowired
	private Scheduler scheduler;

//	@Autowired
//	private SchedulerQuartzJob schedulerQuartzJob;

	/**
	 * 开始执行所有任务
	 */
	public void startJob() throws SchedulerException {
		startJob(scheduler);
		scheduler.start();
		log.debug("开始执行所有任务成功");

	}

	/**
	 * 获取Job信息
	 */
	public String getJobInfo(String name, String group) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(name, group);
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
				scheduler.getTriggerState(triggerKey).name());
	}

	public void addJob(DeviceQuartzRow quartz) {

		try {
			// 任务名，任务组，任务执行类
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("deviceId", quartz.getDeviceId());
			dataMap.put("job", quartz.getDeviceJob().getJob());

			JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).setJobData(dataMap)
					.withIdentity(quartz.getDeviceJob().getName(), quartz.getGroup()).build();

			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getDeviceJob().getCron());
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(quartz.getDeviceJob().getName(), quartz.getGroup()).withSchedule(cronScheduleBuilder)
					.build();

			// 调度容器设置JobDetail和Trigger
			scheduler.scheduleJob(jobDetail, cronTrigger);

			// 启动
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
			log.debug("定时任务增加成功");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改某个任务的执行时间
	 */
	public boolean modifyJob(String name, String group, String time) throws SchedulerException {
		Date date = null;
		TriggerKey triggerKey = new TriggerKey(name, group);
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		String oldTime = cronTrigger.getCronExpression();
		if (!oldTime.equalsIgnoreCase(time)) {
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
					.withSchedule(cronScheduleBuilder).build();
			date = scheduler.rescheduleJob(triggerKey, trigger);
		}
		log.debug("修改某个任务的执行时间成功");

		return date != null;
	}

	/**
	 * 暂停所有任务
	 */
	public void pauseAllJob() throws SchedulerException {
		scheduler.pauseAll();
		log.debug("暂停所有任务成功");

	}

	/**
	 * 暂停某个任务
	 */
	public void pauseJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null)
			return;
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复所有任务
	 */
	public void resumeAllJob() throws SchedulerException {
		scheduler.resumeAll();
	}

	/**
	 * 恢复某个任务
	 */
	public void resumeJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null)
			return;
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 关闭
	 */
	public void shutdown() throws SchedulerException {
		scheduler.shutdown();
	}

	/**
	 * 删除某个任务
	 */
	public void deleteJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		log.debug("jobDetail={}",scheduler.getJobDetail(jobKey));
		log.debug("jobDetail={}",scheduler.getJobGroupNames());

		if (jobDetail == null)
			return;
		scheduler.deleteJob(jobKey);
		log.debug("定时任务删除成功");
	}

	@Autowired
	private DeviceQuartzService deviceQuartzService;

	private void startJob(Scheduler scheduler) throws SchedulerException {
		log.debug("SpringContextUtils.getBean(schedulerQuartzJob.getClass())");
//		Class<? extends SchedulerQuartzJob> x = schedulerQuartzJob.getClass();
//		SchedulerQuartzJob xClass = SpringContextUtils.getBean(schedulerQuartzJob.getClass());

		
		List<DeviceQuartzRow> quartzs = deviceQuartzService.listRowsOnAll();

		for (DeviceQuartzRow quartz : quartzs) {
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("deviceId", quartz.getDeviceId());
			dataMap.put("job", quartz.getDeviceJob().getJob());

			JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).setJobData(dataMap)
					.withIdentity(quartz.getDeviceJob().getName(), quartz.getGroup())
					// .usingJobData("deviceId", quartz.getDeviceId())// 可以往ScheduleQuartzJob传参
					// .usingJobData("job", quartz.getDeviceJob().getJob())
					.build();

			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getDeviceJob().getCron());
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(quartz.getDeviceJob().getName(), quartz.getGroup()).withSchedule(cronScheduleBuilder)
					.build();
			scheduler.scheduleJob(jobDetail, cronTrigger);
		}

		// JobDetail jobDetail =
		// JobBuilder.newJob(SchedulerQuartzJob.class).withIdentity("job1", "group1")
		// .usingJobData("word", "Hello,World")// 可以往ScheduleQuartzJob传参
		// .build();
		// // 基于表达式构建触发器
		// CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0
		// 1 * * * ?");// mailJob.getCron()
		// // CronTrigger表达式触发器 继承于Trigger
		// // TriggerBuilder 用于构建触发器实例
		// CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job1",
		// "group1")
		// .withSchedule(cronScheduleBuilder).build();
		// scheduler.scheduleJob(jobDetail, cronTrigger);
	}
}
