package com.unicom.mongo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Classsname MongoResult
 * @Description TODO
 * @Date 2020/5/22 19:41
 * @author by ctf
 **/
@Data
public class MongoResult {
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;
    private long count;
}
