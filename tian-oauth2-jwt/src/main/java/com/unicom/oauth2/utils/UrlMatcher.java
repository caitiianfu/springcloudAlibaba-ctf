package com.unicom.oauth2.utils;

/** @Description: @Author: ctf @Date: 2019/5/9 */
public interface UrlMatcher {
  Object compile(String paramString);

  boolean pathMatchesUrl(Object paramObject, String paramString);

  String getUniversalMatchPattern();

  boolean requiresLowerCaseUrl();
}
