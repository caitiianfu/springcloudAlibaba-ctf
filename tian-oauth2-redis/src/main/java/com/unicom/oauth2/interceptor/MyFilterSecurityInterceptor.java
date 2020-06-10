package com.unicom.oauth2.interceptor;

import com.unicom.oauth2.config.IgnoreUrlsConfig;
import com.unicom.oauth2.filter.MyAccessDecisionManager;
import com.unicom.oauth2.filter.MyInvocationSecurityMetadataSourceService;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @Description: 权限验证过滤器，继承AbstractSecurityInterceptor、实现Filter是必须的
 * 首先，登陆后，每次访问资源都会被这个拦截器拦截，会执行doFilter这个方法，这个方法调用了invoke方法，其中fi断点显示是一个url
 * 最重要的是beforeInvocation这个方法，它首先会调用MyInvocationSecurityMetadataSource类的getAttributes方法获取被拦截url所需的权限
 * 在调用MyAccessDecisionManager类decide方法判断用户是否具有权限,执行完后就会执行下一个拦截器 @Author: ctf @Date: 2019/5/9
 */
@Component
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

  @Autowired private FilterInvocationSecurityMetadataSource securityMetadataSource;
  @Autowired private IgnoreUrlsConfig ignoreUrlsConfig;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  /**
   * 登录后 每次请求都会调用这个拦截器进行请求过滤
   *
   * @param servletRequest
   * @param servletResponse
   * @param filterChain
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Allow-Headers", ":x-requested-with,content-type");

    // 白名单请求直接放行
    PathMatcher pathMatcher = new AntPathMatcher();
    /* for (String path : ignoreUrlsConfig.getUrls()) {
      if (pathMatcher.match(path, request.getRequestURI())) {
        fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        return;
      }
    }*/
    if (!pathMatcher.match(("/oauth/token"), request.getRequestURI())) {
      invoke(fi);
    } else {
      // 白名单放行，执行下一个过滤器
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {}

  @Override
  public Class<?> getSecureObjectClass() {
    return FilterInvocation.class;
  }

  @Override
  public SecurityMetadataSource obtainSecurityMetadataSource() {
    return this.securityMetadataSource;
  }

  public void setSecurityMetadataSource(MyInvocationSecurityMetadataSourceService metadataSource) {
    this.securityMetadataSource = metadataSource;
  }

  @Bean
  public IgnoreUrlsConfig ignoreUrlsConfig() {
    return new IgnoreUrlsConfig();
  }

  @Autowired
  public void setAccessDecisionManager(MyAccessDecisionManager myAccessDecisionManager) {
    super.setAccessDecisionManager(myAccessDecisionManager);
  }

  /**
   * 拦截请求处理
   *
   * @param fi
   * @throws IOException
   * @throws ServletException
   */
  public void invoke(FilterInvocation fi) throws IOException, ServletException {
    // fi里面有一个被拦截的url
    // 里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
    // 再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
    InterceptorStatusToken token = super.beforeInvocation(fi);
    try {
      // 执行下一个拦截器
      fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
    } finally {
      super.afterInvocation(token, null);
    }
  }
}
