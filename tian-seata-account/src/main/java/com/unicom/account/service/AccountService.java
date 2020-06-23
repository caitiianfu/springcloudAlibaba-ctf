/**
 * Title: AccountService.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unicom.generator.entity.Account;
import java.math.BigDecimal;

public interface AccountService extends IService<Account> {

  /**
   * 扣减账户余额
   *
   * @param userId 用户id
   * @param money 金额
   */
  void decrease(Long userId, BigDecimal money);
}
