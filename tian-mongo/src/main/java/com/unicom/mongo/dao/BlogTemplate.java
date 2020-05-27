package com.unicom.mongo.dao;

import com.unicom.mongo.entity.Blog;
import com.unicom.mongo.entity.MongoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @Classsname BlogTemplate
 * @Description TODO
 * @Date 2020/5/21 19:03
 * @Created by 10750
 **/
@Service
public class BlogTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 模糊查询
     * @param name
     * @return
     */
    public List<Blog> getListByName(String name){
        Pattern pattern=Pattern.compile("^.*"+name+".*$",Pattern.CASE_INSENSITIVE);
        return mongoTemplate.find(new Query(Criteria.where("name").regex(pattern)).with(new Sort(Sort.Direction.DESC,"startTime")),Blog.class);
    }

    /**
     * 保存
     * @param blog
     * @return
     */
    public Blog save(Blog blog){
        return mongoTemplate.save(blog);
    }

    /**
     * 聚合查询 按天分组
     * @param blog
     * @return
     */
    public List<MongoResult> getAgg(Blog blog){
        Aggregation agg=Aggregation.newAggregation(
                Aggregation.match(Criteria.where("startTime").gt(blog.getStartTime())),
            Aggregation.project("startTime")
                    .andExpression("dayOfYear(add(startTime,"+8*60*60*1000+"))")
                    .as("day"),
                Aggregation.group("day")
                        .count()
                        .as("count")
                        .push("startTime")
                        .as("startTime")
                        .push("day").as("day"),
                Aggregation.sort(Sort.Direction.DESC,"startTime")
                    );
            AggregationResults<MongoResult> out=mongoTemplate.aggregate(agg,"blog",MongoResult.class);
            List<MongoResult> result=out.getMappedResults();
            return result;
    }

}
