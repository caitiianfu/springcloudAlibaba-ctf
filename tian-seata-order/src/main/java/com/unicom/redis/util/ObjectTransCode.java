package com.unicom.redis.util;

import com.unicom.common.exception.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import lombok.extern.slf4j.Slf4j;

/** @author by ctf */
@Slf4j
public class ObjectTransCode {

  /**
   * 序列化对象
   *
   * @param value
   * @return
   */
  public static byte[] serialize(Object value) {
    if (value == null) {
      Assert.fail("序列化数据不能为空");
    }
    ByteArrayOutputStream bos = null;
    ObjectOutputStream os = null;
    byte[] v = null;
    try {
      bos = new ByteArrayOutputStream();
      os = new ObjectOutputStream(bos);
      os.writeObject(value);
      v = bos.toByteArray();
    } catch (IOException e) {
      Assert.fail("序列化出现异常");
    } finally {
      try {
        if (os != null) {
          os.close();
        }
        if (bos != null) {
          bos.close();
        }
      } catch (Exception e) {
        Assert.fail(e.getMessage());
      }
    }
    return v;
  }

  /**
   * 反序列化对象
   *
   * @param v
   * @return
   */
  public static Object deserialize(byte[] v) {
    if (v == null || v.length == 0) {
      Assert.fail("反序列不能为空");
    }
    ByteArrayInputStream bis = null;
    ObjectInputStream is = null;
    Object value = null;
    try {
      bis = new ByteArrayInputStream(v);
      is = new ObjectInputStream(bis);
      value = is.readObject();
    } catch (IOException e) {
      log.error("反序列出错 {}", e);
      Assert.fail(e.getMessage());
    } catch (ClassNotFoundException e) {
      log.error("反序列出错 {}", e);

      Assert.fail(e.getMessage());
    } finally {
      try {
        if (is != null) {
          is.close();
        }
        if (bis != null) {
          bis.close();
        }
      } catch (IOException e) {
        log.error("反序列出错 {}", e);
        Assert.fail(e.getMessage());
      }
    }
    return value;
  }
}
