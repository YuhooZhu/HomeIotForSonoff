//package cn.mingzhu.iot.app.config;
//
//import org.apache.http.conn.HttpClientConnectionManager;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
///**
// * spring的RestTemplate连接池相关配置
// * 
// * @project common-utils
// * @fileName RestTemplateConfiguration.java
// * @Description
// * @author light-zhang
// * @date 2019年4月29日
// * @version 1.0.0
// */
//@Configuration
//public class RestTemplatePoolConfig {
//    /**
//     * 让spring管理RestTemplate,参数相关配置
//     * 
//     * @param builder
//     * @return
//     */
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        RestTemplate restTemplate = builder.build();// 生成一个RestTemplate实例
//        restTemplate.setRequestFactory(clientHttpRequestFactory());
//        return restTemplate;
//    }
//
//    /**
//     * 客户端请求链接策略
//     * 
//     * @return
//     */
//    @Bean
//    public ClientHttpRequestFactory clientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        clientHttpRequestFactory.setHttpClient(httpClientBuilder().build());
//        clientHttpRequestFactory.setConnectTimeout(6000); // 连接超时时间/毫秒
//        clientHttpRequestFactory.setReadTimeout(6000); // 读写超时时间/毫秒
//        clientHttpRequestFactory.setConnectionRequestTimeout(5000);// 请求超时时间/毫秒
//        return clientHttpRequestFactory;
//    }
//
//    /**
//     * 设置HTTP连接管理器,连接池相关配置管理
//     * 
//     * @return 客户端链接管理器
//     */
//    @Bean
//    public HttpClientBuilder httpClientBuilder() {
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        httpClientBuilder.setConnectionManager(poolingConnectionManager());
//        return httpClientBuilder;
//    }
//
//    /**
//     * 链接线程池管理,可以keep-alive不断开链接请求,这样速度会更快 MaxTotal 连接池最大连接数 DefaultMaxPerRoute
//     * 每个主机的并发 ValidateAfterInactivity
//     * 可用空闲连接过期时间,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
//     * 
//     * @return
//     */
//    @Bean
//    public HttpClientConnectionManager poolingConnectionManager() {
//        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
//        poolingConnectionManager.setMaxTotal(1000);
//        poolingConnectionManager.setDefaultMaxPerRoute(5000);
//        poolingConnectionManager.setValidateAfterInactivity(30000);
//        return poolingConnectionManager;
//    }
//}

package cn.mingzhu.iot.app.config;
 
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
 
import java.util.concurrent.TimeUnit;
 
/**
 *   HttpComponentClientHttpRequestFactory  设置时间
 *
 * */
public class RestTemplatePoolConfig {
    private static RestTemplate restTemplate;
 
    static {
        // 长链接保持时间长度20秒
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                new PoolingHttpClientConnectionManager(5, TimeUnit.SECONDS);
        // 设置最大链接数
        poolingHttpClientConnectionManager.setMaxTotal(2*getMaxCpuCore() + 3 );
        // 单路由的并发数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(2*getMaxCpuCore());
 
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
 
        // 重试次数3次，并开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3,true));
        HttpClient httpClient = httpClientBuilder.build();
        // 保持长链接配置，keep-alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
 
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
 
        // 链接超时配置 5秒
        httpComponentsClientHttpRequestFactory.setConnectTimeout(5000);
        // 连接读取超时配置
//        httpComponentsClientHttpRequestFactory.setReadTimeout(10000);
        // 连接池不够用时候等待时间长度设置，分词那边 500毫秒 ，我们这边设置成1秒
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(3000);
 
        // 缓冲请求数据，POST大量数据，可以设定为true 我们这边机器比较内存较大
        httpComponentsClientHttpRequestFactory.setBufferRequestBody(true);
 
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
    }
 
    public static RestTemplate getRestTemplate(){
        return restTemplate;
    }
 
    private static int getMaxCpuCore(){
        int cpuCore = Runtime.getRuntime().availableProcessors();
        return  cpuCore;
    }
}
