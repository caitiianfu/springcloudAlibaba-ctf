/**
 * Title: OrderController.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.account.web;

import com.unicom.account.service.AccountService;
import com.unicom.common.api.ResultUtils;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired private AccountService accountService;

  /** 扣减账户余额 */
  @RequestMapping("/decrease")
  public ResultUtils decrease(
      @RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money) {
    accountService.decrease(userId, money);
    return ResultUtils.successMsg("扣减账户余额成功！", 200);
  }
}
