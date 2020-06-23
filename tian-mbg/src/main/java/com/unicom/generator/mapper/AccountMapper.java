/**
 * Title: OrderDao.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unicom.generator.entity.Account;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper extends BaseMapper<Account> {

  /** 扣减账户余额 */
  void decrease(@Param("userId") Long userId, @Param("money") BigDecimal money);
}
