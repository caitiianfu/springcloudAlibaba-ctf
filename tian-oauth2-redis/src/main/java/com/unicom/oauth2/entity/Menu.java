package com.unicom.oauth2.entity;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/** @author by ctf */
@Data
public class Menu implements Serializable {
  private Integer id;
  private String name;
  private String path;
  private Integer parentId;
  private List<Menu> children;

  public Menu(Integer id, String name, String path, Integer parentId) {
    this.id = id;
    this.name = name;
    this.path = path;
    this.parentId = parentId;
  }
}
