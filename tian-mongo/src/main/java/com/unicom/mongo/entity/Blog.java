package com.unicom.mongo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * @Classsname Blog
 * @Description TODO
 * @Date 2020/5/21 18:54
 * @Created by 10750
 **/
@Document(collection = "blog")
@Data
public class Blog {
    @Id
    private  String id;
    @Indexed
    private  String guid;
    private String name;
    private String title;
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;
    private Map<String,Object> others;
}
