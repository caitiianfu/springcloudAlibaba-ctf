package com.unicom.rocketmq.vo;
/** */
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 *
 * @date 2020/6/29
 * @author ctf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PraiseRecordVO {
  private Long uid;
  private Long liveId;
  private BigDecimal money;
}
