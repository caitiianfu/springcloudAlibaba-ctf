/**
 * Title: OrderServiceImpl.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.storage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.generator.entity.Storage;
import com.unicom.generator.mapper.StorageMapper;
import com.unicom.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage>
    implements StorageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

  @Autowired private StorageMapper storageMapper;

  /** 扣减库存 */
  @Override
  public void decrease(Long productId, Integer count) {
    LOGGER.info("------->storage-service中扣减库存开始");
    storageMapper.decrease(productId, count);
    LOGGER.info("------->storage-service中扣减库存结束");
  }
}
