package com.unicom.oauth2.entity;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/** @author by ctf */
@Data
public class Role implements Serializable {
  private Integer id;
  private String name;
  private List<Menu> menus;

  public Role() {}

  public Role(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
}
