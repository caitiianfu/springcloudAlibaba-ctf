package com.unicom.mongo.repository;

import com.unicom.mongo.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Classsname BlogRepository
 * @Description TODO
 * @Date 2020/5/21 19:00
 * @Created by 10750
 **/
public interface BlogRepository  extends MongoRepository<Blog,String> {
    List<Blog> findByNameLikeOrderByStartTimeDesc(String name);
}
