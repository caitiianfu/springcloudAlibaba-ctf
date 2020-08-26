package com.unicom.minio.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
public class MinioData {
  /**
   * 服务器地址
   */
  @Value("${minio.ip}")
  private String ip;

  /**
   * 登录账号
   */
  @Value("${minio.accessKey}")
  private String accessKey;

  /**
   * 登录密码
   */
  @Value("${minio.secretKey}")
  private String secretKey;

}
