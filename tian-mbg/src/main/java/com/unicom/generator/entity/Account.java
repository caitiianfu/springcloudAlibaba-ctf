/**
 * Title: Order.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.generator.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class Account extends Model<Account> {
  private static final long serialVersionUID = 1L;

  private Long id;

  /** 用户id */
  private Long userId;

  /** 总额度 */
  private BigDecimal total;

  /** 已用额度 */
  private BigDecimal used;

  /** 剩余额度 */
  private BigDecimal residue;
}
