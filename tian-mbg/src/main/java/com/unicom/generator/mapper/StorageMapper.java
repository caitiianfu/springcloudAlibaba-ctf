/**
 * Title: OrderDao.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unicom.generator.entity.Storage;
import org.apache.ibatis.annotations.Param;

public interface StorageMapper extends BaseMapper<Storage> {

  /** 扣减库存 */
  void decrease(@Param("productId") Long productId, @Param("count") Integer count);
}
