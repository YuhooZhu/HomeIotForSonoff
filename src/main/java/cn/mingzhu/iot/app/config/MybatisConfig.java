//package cn.mingzhu.iot.app.config;
//import java.util.Properties;
//
//import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import tk.mybatis.spring.mapper.MapperScannerConfigurer;
///**
//* 通用mapper配置了类
//* @author songgt
//*/
//@Configuration
//@AutoConfigureAfter(MybatisAutoConfiguration.class)
//public class MybatisConfig {
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer(){
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        //mapper类的包名--即操作数据库的对应到数据表的Mapper.java类的包名，如果这里的包名配错项目启动报错
//        mapperScannerConfigurer.setBasePackage("com.test.mapper.customer");
//
//        //这里properties的相关配置，由于能力有限，暂时还未探究，如果你知道请麻烦告诉我，谢谢
//        Properties properties = new Properties();
//		properties.setProperty("mappers", "cn.mingzhu.iot.app.bas.mapper");
//        properties.setProperty("notEmpty","false");
//        properties.setProperty("IDENTITY", "MYSQL");
//        properties.setProperty("ORDER","BEFORE");
//        mapperScannerConfigurer.setProperties(properties);
//        return mapperScannerConfigurer;
//    }
//}