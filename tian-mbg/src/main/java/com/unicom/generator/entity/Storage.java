/**
 * Title: Order.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.generator.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class Storage extends Model<Storage> {
  private static final long serialVersionUID = 1L;

  private Long id;

  /** 产品id */
  private Long productId;

  /** 总库存 */
  private Integer total;

  /** 已用库存 */
  private Integer used;

  /** 剩余库存 */
  private Integer residue;
}
