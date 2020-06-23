/**
 * Title: AccountService.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.order.service;

import com.unicom.common.api.ResultUtils;
import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "tian-seata-account")
public interface AccountService {

  /** 扣减账户余额 */
  @RequestMapping("/account/decrease")
  ResultUtils decrease(
      @RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
