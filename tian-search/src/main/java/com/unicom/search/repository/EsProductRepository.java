package com.unicom.search.repository;/**
 *
 **/

import com.unicom.search.entity.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsProductRepository extends ElasticsearchRepository<EsProduct,Long> {
  
/**
 *
 * @Description TODO
 * @param name  名称
 * @param subTile  主题
 * @param keyWords  关键字
 * @param pageable   分页参数
 * @return org.springframework.data.domain.Page<com.unicom.search.entity.EsProduct>
 * @date 2020/7/20
 * @author ctf
 **/

Page<EsProduct> findByNameOrSubTitleOrKeywords(String name,String subTile,String keyWords,Pageable pageable);
  @Query("{\"bool\" : {\"must\" : {\"field\" : {\"name\" : \"?0\"}}}}")
  Page<EsProduct> findByName(String name, Pageable pageable);


}
