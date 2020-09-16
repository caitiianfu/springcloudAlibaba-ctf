package com.unicom.oauth2.service.impl;

import com.unicom.common.constant.AuthConstant;
import com.unicom.common.domain.UserDto;
import com.unicom.oauth2.constant.MessageConstant;
import com.unicom.oauth2.domain.SecurityUser;
import com.unicom.oauth2.service.UserAdminService;
import javax.security.auth.login.CredentialExpiredException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {
  @Autowired
  private UserAdminService userAdminService;
  @Autowired
  private HttpServletRequest request;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String clientId=request.getParameter("client_id");
    UserDto userDto=null;
    if (AuthConstant.ADMIN_CLIENT_ID.equals(clientId)){
      userDto=userAdminService.loadUserByUsername(username);
    }
    if (userDto==null){
      throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
    }
    userDto.setClientId(clientId);
    SecurityUser securityUser=new SecurityUser(userDto);
    if (!securityUser.isEnabled()){
      throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
    }else if(!securityUser.isAccountNonLocked()){
      throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
    }else if(!securityUser.isAccountNonExpired()){
      throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
    }else if(!securityUser.isCredentialsNonExpired()){
      throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
    }
    return securityUser;
  }
}
