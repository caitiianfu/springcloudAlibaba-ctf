package com.unicom.admin.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidDBConfig {

  @Bean
  public ServletRegistrationBean druidServlet() {
    ServletRegistrationBean reg = new ServletRegistrationBean();
    reg.setServlet(new StatViewServlet());
    reg.addUrlMappings("/druid/*");
    // reg.addInitParameter("allow", "127.0.0.1"); //白名单
    // IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not
    // permitted to view this page.
    reg.addInitParameter("deny", "192.168.1.73");
    // 登录查看信息的账号密码.
    reg.addInitParameter("loginUsername", "admin");
    reg.addInitParameter("loginPassword", "123456");
    // 是否能够重置数据.
    reg.addInitParameter("resetEnable", "false");
    return reg;
  }

  @Bean
  public FilterRegistrationBean filterRegistrationBean() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new WebStatFilter());
    Map<String, String> initParams = new HashMap<String, String>();
    // 设置忽略请求
    initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
    filterRegistrationBean.setInitParameters(initParams);
    filterRegistrationBean.addInitParameter("profileEnable", "true");
    filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
    filterRegistrationBean.addInitParameter("principalSessionName", "");
    filterRegistrationBean.addInitParameter("aopPatterns", "com.unicom.generator.service");
    filterRegistrationBean.addUrlPatterns("/*");
    return filterRegistrationBean;
  }

  /*@Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
      return new DruidDataSource();
    }

    // 配置事物管理器
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {
      return new DataSourceTransactionManager(dataSource());
    }
  */ }
