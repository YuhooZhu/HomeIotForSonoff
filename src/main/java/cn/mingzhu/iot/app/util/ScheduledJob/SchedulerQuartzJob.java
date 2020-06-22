package cn.mingzhu.iot.app.util.ScheduledJob;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mingzhu.iot.app.bas.Response;
import cn.mingzhu.iot.app.common.iot.device.IotDeviceService;
import cn.mingzhu.iot.app.config.HttpClient;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.util.StringUtil;

@Service
@DisallowConcurrentExecution
@Slf4j
public class SchedulerQuartzJob implements Job {

	@Autowired
	private IotDeviceService iotDeviceService;
	
	@Autowired
	private HttpClient httpClient;

	@Override
	public void execute(JobExecutionContext context) {
		try {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			Integer deviceId = dataMap.getInt("deviceId"); // 通过此我们可以获得前面传来的参数
			String job = dataMap.getString("job"); // 通过此我们可以获得前面传来的参数

			if (StringUtil.isNotEmpty(job) || deviceId != null) {
				Response response = new Response();
//				log.debug("job={}",job);

				if ("on".equals(job)) {
//					log.debug("job={}",job);
					iotDeviceService.updateStateToOn(deviceId);
				}
				if ("off".equals(job)) {
//					log.debug("job={}",job);
					iotDeviceService.updateStateToOff(deviceId);
				}
//				try {
//					response = httpClient.putSelf(job);
//				} catch (Exception e) {
//					throw new ApiIllegalArgumentException("http请求错误", e);
//				}
			}
//			System.out.println("学习使用quartzxxxxxxxxxx");
		} catch (Exception e) {
			throw (e);
		}

	}
}
