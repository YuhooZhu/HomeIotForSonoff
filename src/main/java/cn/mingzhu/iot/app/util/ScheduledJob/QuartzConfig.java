package cn.mingzhu.iot.app.util.ScheduledJob;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {
	private JobFactory jobFactory;

	public QuartzConfig(JobFactory jobFactory) {
		this.jobFactory = jobFactory;
	}

	/**
	 * 配置SchedulerFactoryBean 将一个方法产生为Bean并交给Spring容器管理
	 * 
	 * @throws IOException
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		// Spring提供SchedulerFactoryBean为Scheduler提供配置信息,并被Spring容器管理其生命周期
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		// 设置自定义Job Factory，用于Spring管理Job bean

		try {
			factory.setJobFactory(jobFactory);
//			factory.setStartupDelay(3);
			factory.setQuartzProperties(properties());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return factory;
	}

	@Bean
	public Properties properties() throws IOException {
		Properties prop = new Properties();
		prop.load(new ClassPathResource("/customerQuartz.properties").getInputStream());
		return prop;
	}

	@Bean(name = "scheduler")
	public Scheduler scheduler() {
		return schedulerFactoryBean().getScheduler();
	}
}
