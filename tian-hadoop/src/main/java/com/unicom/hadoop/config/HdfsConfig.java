package com.unicom.hadoop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**

 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.context.annotation.Configuration;

 /**
 * HDFS配置类
 * @author linhaiy
 * @date 2019.05.18
 */
@Configuration
public class HdfsConfig {
  @Value("${hdfs.path}")
  private String path;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}


