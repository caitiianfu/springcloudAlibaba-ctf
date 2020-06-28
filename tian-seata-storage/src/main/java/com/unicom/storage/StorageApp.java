package com.unicom.storage;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/** @Classsname RedisApp @Description TODO @Date 2020/5/23 19:22 @Created by 10750 */
// @EnableDiscoveryClient
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(
    scanBasePackages = {"com.unicom"},
    exclude = DataSourceAutoConfiguration.class)
public class StorageApp {
  public static void main(String[] args) {
    SpringApplication.run(StorageApp.class, args);
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DruidDataSource druidDataSource() {
    DruidDataSource druidDataSource = new DruidDataSource();
    return druidDataSource;
  }

  @Primary
  @Bean("dataSource")
  public DataSourceProxy dataSource(DruidDataSource druidDataSource) {
    return new DataSourceProxy(druidDataSource);
  }
}
