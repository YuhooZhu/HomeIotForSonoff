package cn.mingzhu.iot;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@MapperScan("cn.mingzhu.iot.app.bas.mapper")
@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class IotApplication extends SpringBootServletInitializer implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(IotApplication.class, args);
		log.info("");
		log.info("");
		log.info("Dianmai Traceability Service Started ...");
		log.info("");
		log.info("");
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		log.info("");
		log.info("Started ...");
		log.info("");
	}

}
