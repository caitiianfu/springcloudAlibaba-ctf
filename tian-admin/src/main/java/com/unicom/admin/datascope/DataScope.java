package com.unicom.admin.datascope;

import java.util.List;
import lombok.Data;

/** @author by ctf */
@Data
public class DataScope {

  /** 限制范围的字段名称 */
  private String scopeName = "deptid";
  /** 数据类型 */
  private Integer dataType;

  /** 具体的数据范围 */
  private List<Integer> deptIds;
}
