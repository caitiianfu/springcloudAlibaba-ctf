/**
* Title: MybatisPlusConfig.java
* Copyright: Copyright (c) 2018
* Company: Unicom
* @author ctf
* @date 2019年1月28日
* @version 1.0
*/
package com.unicom.mongo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
* <p>Title: MybatisPlusConfig<／p>
* Copyright: Copyright (c) 2018
* Company: Unicom
* @author ctf
* @date 2019年1月28日
* @version 1.0
*/
@Configuration
@EnableTransactionManagement
@MapperScan("com.unicom.generator.mapper")
public class MybatisPlusConfig {

	/**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
    	PerformanceInterceptor performanceInterceptor=new PerformanceInterceptor();
		/*<!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 -->*/
		performanceInterceptor.setMaxTime(1000);
		/*<!--SQL是否格式化 默认false-->*/
		performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }




}

