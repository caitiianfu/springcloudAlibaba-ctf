/**
 * Title: StorageService.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unicom.generator.entity.Storage;

public interface StorageService extends IService<Storage> {
  /** 扣减库存 */
  void decrease(Long productId, Integer count);
}
