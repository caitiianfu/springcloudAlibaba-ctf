package com.unicom.admin.service;

import com.unicom.common.api.ResultUtils;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("tian-auth")
public interface AuthService {
    @PostMapping("/oauth/token")
    public ResultUtils getAccessToken(@RequestParam Map<String,String> params);
}
