package com.unicom.common.constant;
/** 权限相关常量
 * */
public interface AuthConstant {
  /**存储权限前缀*/
  String AUTHORITY_PREFIX="ROLE_";
  /**jwt存储权限属性*/
  String AUTHORITY_CLAIM_NAME="authorities";
  /**后台管理client_id*/
  String ADMIN_CLIENT_ID="amdin-app";
  /**前台*/
  String PORTAL_CLIENT_ID="portal-app";
  /**后台管理接口路径匹配*/
  String ADMIN_URL_PATTERN="/tian-admin/**";
  /**redis缓存权限规则key*/
  String RESOURCE_ROLES_MAP_KEY="auth:resourceRolesMap";
  /**认证信息请求头*/
  String JWT_TOKEN_HEADER="Authorization";
  /**jwt令牌前缀*/
  String JWT_TOKEN_PREFIX="Bearer ";
  /**用户信息·http请求头*/
  String USER_TOKEN_HEADER="user";
}
