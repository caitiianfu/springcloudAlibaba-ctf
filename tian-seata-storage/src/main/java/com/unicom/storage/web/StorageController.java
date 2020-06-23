/**
 * Title: OrderController.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.storage.web;

import com.unicom.common.api.ResultUtils;
import com.unicom.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
public class StorageController {

  @Autowired private StorageService storageService;

  /** 扣减库存 */
  @RequestMapping("/decrease")
  public ResultUtils decrease(Long productId, Integer count) {
    storageService.decrease(productId, count);
    return ResultUtils.successMsg("扣减库存成功！", 200);
  }
}
