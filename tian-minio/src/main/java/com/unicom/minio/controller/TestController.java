package com.unicom.minio.controller;/**
 *
 */

import com.unicom.minio.common.MinioUtil;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description TODO
 * @author ctf
 * @date 2020/8/10
 */
@RestController
public class TestController {
  @Autowired
  private MinioUtil minioUtil;
  @PostMapping("/upload")
  public String minioUpload(MultipartFile file, String fileName, String bucketName) {
    return minioUtil.minioUpload(file,fileName,bucketName);
  }

  @PostMapping("/download")
  public String downloadFile(String objectName, String bucketName, HttpServletResponse response) {
    return minioUtil.downloadFile(objectName,bucketName,response);
  }
  }
