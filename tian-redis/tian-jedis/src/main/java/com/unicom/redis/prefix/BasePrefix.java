package com.unicom.redis.prefix;

/** @author by ctf @Classsname BasePrefix @Description TODO @Date 2020/5/25 11:21 */
public class BasePrefix implements KeyPrefix {
  private String prefix;
  private int expireSeconds;

  public BasePrefix(String prefix) {
    this.prefix = prefix;
    this.expireSeconds = 0;
  }

  public BasePrefix(String prefix, int expireSeconds) {
    this.prefix = prefix;
    this.expireSeconds = expireSeconds;
  }

  @Override
  public String getPrefix() {
    return getClass().getSimpleName() + ":" + prefix;
  }

  @Override
  public int getExpireSeconds() {
    return expireSeconds;
  }
}
