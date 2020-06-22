package cn.mingzhu.iot.app.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class LoadingConfig {
	@Configuration
	public class XxxConfig   {

	    @Bean
	    public ServletWebServerFactory webServerFactory() {
	        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
	        fa.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "#[]{}"));
	        return fa;
	    }
	}
}
