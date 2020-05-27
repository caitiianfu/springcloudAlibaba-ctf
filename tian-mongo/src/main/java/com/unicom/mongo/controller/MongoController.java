package com.unicom.mongo.controller;

import com.unicom.common.api.ResultUtils;
import com.unicom.mongo.dao.BlogTemplate;
import com.unicom.mongo.entity.Blog;
import com.unicom.mongo.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Classsname MongoController
 * @Description TODO
 * @Date 2020/5/21 20:02
 * @Created by 10750
 **/
@RestController
@RequestMapping("/blog")
public class MongoController {
    @Autowired
    private BlogTemplate blogTemplate;
    @Autowired
    private BlogRepository blogRepository;
    @RequestMapping("/temSave")
    public ResultUtils temSave(@RequestBody Blog blog){
        return ResultUtils.success(blogTemplate.save(blog));
    }
    @RequestMapping("/repoSave")
    public ResultUtils repoSave(@RequestBody Blog blog){
        return ResultUtils.success(blogRepository.save(blog));
    }
    @RequestMapping("/temList")
    public ResultUtils temList(String name){
        return ResultUtils.success(blogTemplate.getListByName(name));
    }
    @RequestMapping("/repoList")
    public ResultUtils repoList(String name){
        return ResultUtils.success(blogRepository.findByNameLikeOrderByStartTimeDesc(name));
    }
    @RequestMapping("/aggList")
    public ResultUtils aggList(@RequestBody Blog blog){
        return ResultUtils.success(blogTemplate.getAgg(blog));
    }
}
