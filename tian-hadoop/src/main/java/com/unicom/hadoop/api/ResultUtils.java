package com.unicom.hadoop.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultUtils<T> {
  private long code;
  private String msg;
  private T data;

  public ResultUtils() {}

  public ResultUtils(long code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  /** 成功返回结果 */
  public static <T> ResultUtils<T> success(T data) {
    return new ResultUtils<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
  }
  /** 成功返回有消息的结果 */
  public static <T> ResultUtils<T> successMsg(String msg, T data) {
    return new ResultUtils<T>(ResultCode.SUCCESS.getCode(), msg, data);
  }
  /** 失败返回结果 错误码 */
  public static <T> ResultUtils<T> failed(ResultEnum resultEnum) {
    return new ResultUtils<T>(resultEnum.getCode(), resultEnum.getMsg(), null);
  }

  /** 失败返回结果 */
  public static <T> ResultUtils<T> failed() {
    return new ResultUtils<T>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMsg(), null);
  }

  /** 失败返回有消息的结果 */
  public static <T> ResultUtils<T> failedMsg(String msg) {
    return new ResultUtils<T>(ResultCode.FAILED.getCode(), msg, null);
  }
  /** 参数验证失败返回结果 */
  public static <T> ResultUtils<T> validateFailed() {
    return failed(ResultCode.VALIDATE_FAILED);
  }

  /**
   * 参数验证失败返回结果
   *
   * @param message 提示信息
   */
  public static <T> ResultUtils<T> validateFailed(String message) {
    return new ResultUtils<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
  }

  /** 未登录返回结果 */
  public static <T> ResultUtils<T> unauthorized(T data) {
    return new ResultUtils<T>(
        ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMsg(), data);
  }

  /** 未授权返回结果 */
  public static <T> ResultUtils<T> forbidden(T data) {
    return new ResultUtils<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMsg(), data);
  }
}
