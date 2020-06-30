/**
 * Title: OrderServiceImpl.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.account.service.AccountService;
import com.unicom.generator.entity.Account;
import com.unicom.generator.mapper.AccountMapper;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 账户业务实现类 Created by macro on 2019/11/11. */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
  @Autowired private AccountMapper accountMapper;

  /** 扣减账户余额 */
  @Override
  public void decrease(Long userId, BigDecimal money) {
    LOGGER.info("------->account-service中扣减账户余额开始");
    // 模拟超时异常，全局事务回滚
      try {
      Thread.sleep(30 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    accountMapper.decrease(userId, money);
    LOGGER.info("------->account-service中扣减账户余额结束");
  }
}
