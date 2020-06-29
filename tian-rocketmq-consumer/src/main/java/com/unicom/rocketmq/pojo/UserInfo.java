package com.unicom.rocketmq.pojo;
/** */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description TODO
 *
 * @date 2020/6/29
 * @author ctf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInfo {
  private static final int SYMBOL = 1;
  private Integer id;
  private String name;
}
