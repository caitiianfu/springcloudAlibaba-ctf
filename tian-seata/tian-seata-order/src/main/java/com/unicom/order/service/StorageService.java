/**
 * Title: StorageService.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.order.service;

import com.unicom.common.api.ResultUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "tian-seata-storage")
public interface StorageService {

  /** 扣减库存 */
  @GetMapping(value = "/storage/decrease")
  ResultUtils decrease(
      @RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
