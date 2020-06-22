package cn.mingzhu.iot.app.bas.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class TrcControllerAdvice {

//	@Value("${spring.profiles.active}")
//	private String springProfilesActive;
//	
//	@ModelAttribute(name = "isDev")
//    public boolean isDev() {
//        return !"prod".equalsIgnoreCase(springProfilesActive);
//    }
//	
	@Value("${iot.service.version}")
	private String version;

	@ModelAttribute(name = "version")
    public String version() {
        return version;
    }

	@Value("${iot.service.static-url}")
	private String staticUrl;

	@ModelAttribute(name = "staticUrl")
    public String staticUrl() {
        return staticUrl;
    }

	@Value("${iot.service.hot-url}")
	private String hotUrl;
	
	@ModelAttribute(name = "hotUrl")
    public String hotUrl() {
        return hotUrl;
    }

	@ModelAttribute(name = "rnd")
    public String rnd() {
        return version;
    }
	
}

