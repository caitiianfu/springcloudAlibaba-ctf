package com.unicom.common.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @Description 线程池配置
 * @param null
 * @return
 * @date 2020/8/15
 * @author ctf
 **/

@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {
  /**
   * 默认情况下，线程池数量为0，当有线程进入则创建线程
   * 当线程的数量大于coreThreadPool,则会缓存到队列中
   * 当队列满了，则会创建线程，如果线程大于maxThreadPool则会采用拒绝策略**/

  /**核心线程数（默认线程数）**/
  private static final int corePoolSize=20;
  /**最大线程数**/
  private static final int maxPoolSize=100;
  /**允许线程空闲时间（单位默认为秒）**/
  private static final int keepAliveTime=10;
  /**缓冲队列大小**/
  private static final int queueCapacity=200;
  /**线程池前缀**/
  private static final String threadNamePrefix="Async-Service-";

  @Bean("taskExecutor")
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setKeepAliveSeconds(keepAliveTime);
    executor.setQueueCapacity(queueCapacity);
    executor.setThreadNamePrefix(threadNamePrefix);
    // 拒绝策略 ：由调用线程处理该任务
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    //初始化
    executor.initialize();
    return executor;
  }

}

