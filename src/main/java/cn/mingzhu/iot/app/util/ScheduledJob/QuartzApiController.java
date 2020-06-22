package cn.mingzhu.iot.app.util.ScheduledJob;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quartz")
public class QuartzApiController {
	@Autowired
	private QuartzSchedulerNew quartzSchedulerNew;

	@GetMapping("/start")
	public void startQuartzJob() {
		try {
			quartzSchedulerNew.startJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/info")
	public String getQuartzJob(String name, String group) {
		String info = null;
		try {
			info = quartzSchedulerNew.getJobInfo(name, group);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return info;
	}

	@PostMapping("/modify")
	public boolean modifyQuartzJob(String name, String group, String time) {
		boolean flag = true;
		try {
			flag = quartzSchedulerNew.modifyJob(name, group, time);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@PostMapping(value = "/pause")
	public void pauseQuartzJob(String name, String group) {
		try {
			quartzSchedulerNew.pauseJob(name, group);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@GetMapping(value = "/pauseAll")
	public void pauseAllQuartzJob() {
		try {
			quartzSchedulerNew.pauseAllJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "/resume")
	public void resumeQuartzJob(String name, String group) {
		try {
			quartzSchedulerNew.resumeJob(name, group);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value = "/resumeAll")
	public void resume() {
		try {
			quartzSchedulerNew.resumeAllJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "/delete")
	public void deleteJob(String name, String group) {
		try {
			quartzSchedulerNew.deleteJob(name, group);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
