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
public class Order extends Model<Order> {
  private static final long serialVersionUID = 1L;

  private Long id;

  private Long userId;

  private Long productId;

  private Integer count;

  private BigDecimal money;

  /** 订单状态：0：创建中；1：已完结 */
  private Integer status;
}
