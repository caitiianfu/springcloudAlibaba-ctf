package com.unicom.hadoop.controller;/**
 *
 **/


import com.unicom.hadoop.api.ResultUtils;
import com.unicom.hadoop.pojo.User;
import com.unicom.hadoop.service.HdfsService;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.BlockLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/hadoop/hdfs")
public class HdfsAction {

  private static Logger LOGGER = LoggerFactory.getLogger(HdfsAction.class);

  /**
   * 创建文件夹
   * @param path
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "mkdir", method = RequestMethod.POST)
  @ResponseBody
  public ResultUtils mkdir(@RequestParam("path") String path) throws Exception {
    if (StringUtils.isEmpty(path)) {
      LOGGER.debug("请求参数为空");
      return  ResultUtils.failedMsg( "请求参数为空");
    }
    // 创建空文件夹
    boolean isOk = HdfsService.mkdir(path);
    if (isOk) {
      LOGGER.debug("文件夹创建成功");
      return  ResultUtils.success( "文件夹创建成功");
    } else {
      LOGGER.debug("文件夹创建失败");
      return  ResultUtils.failedMsg( "文件夹创建失败");
    }
  }

  /**
   * 读取HDFS目录信息
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/readPathInfo")
  public ResultUtils readPathInfo(@RequestParam("path") String path) throws Exception {
    List<Map<String, Object>> list = HdfsService.readPathInfo(path);
    return  ResultUtils.successMsg ( "读取HDFS目录信息成功", list);
  }

  /**
   * 获取HDFS文件在集群中的位置
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/getFileBlockLocations")
  public ResultUtils getFileBlockLocations(@RequestParam("path") String path) throws Exception {
    BlockLocation[] blockLocations = HdfsService.getFileBlockLocations(path);
    return  ResultUtils.successMsg( "获取HDFS文件在集群中的位置", blockLocations);
  }

  /**
   * 创建文件
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/createFile")
  public ResultUtils createFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file)
      throws Exception {
    if (StringUtils.isEmpty(path) || null == file.getBytes()) {
      return ResultUtils.failedMsg( "请求参数为空");
    }
    HdfsService.createFile(path, file);
    return ResultUtils.success( "创建文件成功");
  }

  /**
   * 读取HDFS文件内容
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/readFile")
  public ResultUtils readFile(@RequestParam("path") String path) throws Exception {
    String targetPath = HdfsService.readFile(path);
    return  ResultUtils.successMsg( "读取HDFS文件内容", targetPath);
  }

  /**
   * 读取HDFS文件转换成Byte类型
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/openFileToBytes")
  public ResultUtils openFileToBytes(@RequestParam("path") String path) throws Exception {
    byte[] files = HdfsService.openFileToBytes(path);
    return  ResultUtils.successMsg( "读取HDFS文件转换成Byte类型", files);
  }

  /**
   * 读取HDFS文件装换成User对象
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/openFileToUser")
  public ResultUtils openFileToUser(@RequestParam("path") String path) throws Exception {
    User user = HdfsService.openFileToObject(path, User.class);
    return  ResultUtils.successMsg( "读取HDFS文件装换成User对象", user);
  }

  /**
   * 读取文件列表
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/listFile")
  public ResultUtils listFile(@RequestParam("path") String path) throws Exception {
    if (StringUtils.isEmpty(path)) {
      return  ResultUtils.failedMsg( "请求参数为空");
    }
    List<Map<String, String>> returnList = HdfsService.listFile(path);
    return  ResultUtils.successMsg("读取文件列表成功", returnList);
  }

  /**
   * 重命名文件
   * @param oldName
   * @param newName
   * @return
   * @throws Exception
   */
  @PostMapping("/renameFile")
  public ResultUtils renameFile(@RequestParam("oldName") String oldName, @RequestParam("newName") String newName)
      throws Exception {
    if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
      return  ResultUtils.failedMsg( "请求参数为空");
    }
    boolean isOk = HdfsService.renameFile(oldName, newName);
    if (isOk) {
      return  ResultUtils.success( "文件重命名成功");
    } else {
      return  ResultUtils.failedMsg( "文件重命名失败");
    }
  }

  /**
   * 删除文件
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/deleteFile")
  public ResultUtils deleteFile(@RequestParam("path") String path) throws Exception {
    boolean isOk = HdfsService.deleteFile(path);
    if (isOk) {
      return ResultUtils.success( "delete file success");
    } else {
      return  ResultUtils.failedMsg( "delete file fail");
    }
  }

  /**
   * 上传文件
   * @param path
   * @param uploadPath
   * @return
   * @throws Exception
   */
  @PostMapping("/uploadFile")
  public ResultUtils uploadFile(@RequestParam("path") String path, @RequestParam("uploadPath") String uploadPath)
      throws Exception {
    HdfsService.uploadFile(path, uploadPath);
    return  ResultUtils.failedMsg( "upload file success");
  }

  /**
   * 下载文件
   * @param path
   * @param downloadPath
   * @return
   * @throws Exception
   */
  @PostMapping("/downloadFile")
  public ResultUtils downloadFile(@RequestParam("path") String path, @RequestParam("downloadPath") String downloadPath)
      throws Exception {
    HdfsService.downloadFile(path, downloadPath);
    return  ResultUtils.success( "download file success");
  }

  /**
   * HDFS文件复制
   * @param sourcePath
   * @param targetPath
   * @return
   * @throws Exception
   */
  @PostMapping("/copyFile")
  public ResultUtils copyFile(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath)
      throws Exception {
    HdfsService.copyFile(sourcePath, targetPath);
    return  ResultUtils.success( "copy file success");
  }

  /**
   * 查看文件是否已存在
   * @param path
   * @return
   * @throws Exception
   */
  @PostMapping("/existFile")
  public ResultUtils existFile(@RequestParam("path") String path) throws Exception {
    boolean isExist = HdfsService.existFile(path);
    return  ResultUtils.success( "file isExist: " + isExist);
  }
}

