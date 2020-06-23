package com.unicom.account.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author by ctf */
@Configuration
public class DataSourceProxyConfig {
  @Bean
  public DataSourceProxy dataSourceProxy(DataSource dataSource) {
    return new DataSourceProxy(dataSource);
  }

  @Bean
  @ConfigurationProperties(prefix = "mybatis")
  public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
    // 这里用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
    MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
    mybatisSqlSessionFactoryBean.setDataSource(dataSource);
    return mybatisSqlSessionFactoryBean;
  }
}
