package com.unicom.rocketmq.pojo;
/** */
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description TODO
 *
 * @date 2020/6/29
 * @author ctf
 */
@Data
public class PraiseRecord implements Serializable {
  private Long id;
  private Long uid;
  private Long liveId;
  private LocalDateTime createTime;
}
