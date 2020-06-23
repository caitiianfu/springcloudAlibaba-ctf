/**
 * Title: OrderDao.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unicom.generator.entity.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper extends BaseMapper<Order> {

  /** 创建订单 */
  void create(Order order);

  /** 修改订单金额 */
  void update(@Param("userId") Long userId, @Param("status") Integer status);
}
