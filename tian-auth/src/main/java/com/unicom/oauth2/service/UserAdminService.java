package com.unicom.oauth2.service;

import com.unicom.common.domain.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("tian-admin")
public interface UserAdminService {
  @GetMapping("/admin/loadByUsername")
  UserDto loadUserByUsername(@RequestParam String username);
}
