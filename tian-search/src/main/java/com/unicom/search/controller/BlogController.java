package com.unicom.search.controller;

import com.unicom.common.api.ResultCode;
import com.unicom.common.api.ResultUtils;
import com.unicom.common.exception.Assert;
import com.unicom.search.entity.BlogModel;
import com.unicom.search.repository.BlogRepository;
import jdk.nashorn.internal.runtime.options.Option;
import org.apache.ibatis.annotations.Delete;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Classsname BlogController
 * @Description TODO
 * @Date 2020/5/20 0:10
 * @Created by 10750
 **/
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @PostMapping("/add")
    public ResultUtils add(@RequestBody BlogModel blogModel){
        return ResultUtils.success(blogRepository.save(blogModel));
    }
    @GetMapping("/get/{id}")
    public ResultUtils getById(@PathVariable String id){
        if (StringUtils.isEmpty(id))
            Assert.fail(ResultCode.NULL_POINT);
        Optional<BlogModel> blogModelOption=blogRepository.findById(id);
        if (blogModelOption.isPresent()){
            BlogModel blogModel=blogModelOption.get();
            return ResultUtils.success(blogModel);
        }
        return ResultUtils.failedMsg("数据为空");
    }
    @GetMapping("/getAll")
    public ResultUtils getAll(){

        Iterable<BlogModel> blogModelIterable=blogRepository.findAll();
        List<BlogModel> list=new ArrayList<>();
        blogModelIterable.forEach(list::add);
        return ResultUtils.success(list);
    }
    @PostMapping("/update")
    public ResultUtils update(@RequestBody BlogModel blogModel){
        if (StringUtils.isEmpty(blogModel.getId())){
            return ResultUtils.failedMsg("请求id不能为空");
        }
        return ResultUtils.success(blogRepository.save(blogModel));
    }
    @PostMapping("/delete/{id}")
    public ResultUtils delete(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failedMsg("请求id不能为空");
        }blogRepository.deleteById(id);
        return ResultUtils.success("成功");
    }
    @DeleteMapping("/deleteAll")
    public ResultUtils deleteAll(@PathVariable String id){
       blogRepository.deleteAll();
        return ResultUtils.success("成功");
    }
    @GetMapping("/get/title")
    public ResultUtils getSearchTitle(String keyword){
        if (StringUtils.isEmpty(keyword))
            Assert.fail(ResultCode.NULL_POINT);
        return ResultUtils.success(blogRepository.findByTitleLike(keyword));
    }
    @GetMapping("/get/titleCustom")
    public ResultUtils getSearchTitleCustom(String keyword){
        if (StringUtils.isEmpty(keyword))
            Assert.fail(ResultCode.NULL_POINT);
        return ResultUtils.success(blogRepository.findByTitleCustom(keyword));
    }

    @GetMapping("/get/titleTemplate")
    public ResultUtils getSearchTitleTemplate(String keyword){
        if (StringUtils.isEmpty(keyword))
            Assert.fail(ResultCode.NULL_POINT);
        SearchQuery searchQuery=new NativeSearchQueryBuilder()
                                .withQuery(new MatchQueryBuilder("title",keyword)).build();
        return ResultUtils.success(elasticsearchTemplate.queryForList(searchQuery,BlogModel.class));
    }


    /**
     * 全文搜索
     */
    @GetMapping("/get/titleTemplatePage")
    public ResultUtils getSearchTitleTemplatePage(String keyword,int page,int size){

        if (StringUtils.isEmpty(page)){
            page=0;
        }
        if (StringUtils.isEmpty(size)){
            size=10;
        }

        //构造分页类
        Pageable pageable= PageRequest.of(page,size);

        NativeSearchQueryBuilder searchQueryBuilder=new NativeSearchQueryBuilder()
                .withPageable(pageable);
        if (!StringUtils.isEmpty(keyword)){
            searchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(keyword));
        }
        SearchQuery searchQuery=searchQueryBuilder.build();
        return ResultUtils.success(elasticsearchTemplate.queryForPage(searchQuery,BlogModel.class));
    }
}
