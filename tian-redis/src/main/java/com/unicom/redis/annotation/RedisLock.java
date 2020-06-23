package com.unicom.redis.annotation;

import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/** @author by ctf @Classsname RedisLock @Description TODO @Date 2020/5/25 19:37 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisLock {
  /**
   * 锁的key
   *
   * @return
   */
  @NotNull
  @NotEmpty
  String key();

  /**
   * 锁的值
   *
   * @return
   */
  String value() default "";

  /**
   * 锁的时间
   *
   * @return
   */
  int expireSeconds() default 15;
  /** 锁的单位 */
  TimeUnit timeUnit() default TimeUnit.SECONDS;
}
