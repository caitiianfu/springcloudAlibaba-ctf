package com.unicom.minio.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {
  @Autowired
  private MinioData minioData;
  @Bean
  public MinioClient minioClient(){
    try {
      return new MinioClient(minioData.getIp(),minioData.getAccessKey(),minioData.getSecretKey());
    } catch (Exception e) {
      log.error("init minioclient  error:{}",e.fillInStackTrace());
      throw  new RuntimeException(e.fillInStackTrace());
    }
  }
}
