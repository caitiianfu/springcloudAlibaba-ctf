package com.unicom.redis.prefix;

/** @author by ctf @Classsname UserAdminPrefix @Description TODO @Date 2020/5/28 23:52 */
public class UserAdminPrefix extends BasePrefix {
  public UserAdminPrefix(String prefix) {
    super(prefix);
  }

  public UserAdminPrefix(String prefix, int expireTime) {
    super(prefix, expireTime);
  }

  public static UserAdminPrefix userAdminUsr = new UserAdminPrefix("username", 30 * 60);
  public static UserAdminPrefix userAdminResource = new UserAdminPrefix("resource", 30 * 60);
}
