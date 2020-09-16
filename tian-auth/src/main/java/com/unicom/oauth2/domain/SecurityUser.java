package com.unicom.oauth2.domain;

import com.unicom.common.domain.UserDto;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
@Data
public class SecurityUser implements UserDetails {
  private Long id;
  /**用户名*/
  private String username;
  /**密码*/
  private String password;
  /**用户状态*/
  private boolean enabled;
  /**客户端id*/
  private String clientId;
  /**权限数据*/
  private Collection<SimpleGrantedAuthority> authorities;
  public SecurityUser(){}
  public SecurityUser(UserDto userDto){
      this.setId(userDto.getId());
      this.setClientId(userDto.getClientId());
      this.setEnabled(userDto.getStatus()==1);
      this.setUsername(userDto.getUsername());
      this.setPassword(userDto.getPassword());
      if (userDto.getRoles()!=null){
          authorities=new ArrayList<>();
          userDto.getRoles().forEach(item->authorities.add(new SimpleGrantedAuthority(item)));
      }
  }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }
}
