package com.unicom.search.service.impl;/**
 *
 **/

import com.unicom.common.api.ResultUtils;
import com.unicom.search.dao.EsProductDao;
import com.unicom.search.entity.EsProduct;
import com.unicom.search.entity.EsProductRelatedInfo;
import com.unicom.search.repository.EsProductRepository;
import com.unicom.search.service.EsProductService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @Description TODO
 * @date 2020/7/20
 * @author ctf
 **/
@Service
@Slf4j
public class EsProductServiceImpl implements EsProductService {
  @Autowired
  private EsProductRepository esProductRepository;
  @Autowired
  private EsProductDao esProductDao;
  @Autowired
  private ElasticsearchRestTemplate elasticsearchTemplate;
  @Override
  public int importAll() {
    List<EsProduct> esProductList=esProductDao.getAllEsProductList(null);
//    Iterable<EsProduct> esProducts=esProductRepository.saveAll(esProductList);
    Iterable<EsProduct> esProducts=elasticsearchTemplate.save(esProductList);

    Iterator<EsProduct> iterator= esProducts.iterator();
    int count=0;
    while (iterator.hasNext()){
      count++;
      iterator.next();
    }
    return count;
  }

  @Override
  public void delete(Long id) {
    esProductRepository.deleteById(id);
  }

  @Override
  public EsProduct create(Long id) {
    EsProduct esProduct=null;
    List<EsProduct> esProductList=esProductDao.getAllEsProductList(id);
    if (esProductList.size() > 0) {
      esProduct=esProductRepository.save(esProductList.get(0));
    }

    return esProduct;
  }

  @Override
  public void deleteAll(List<Long> ids) {
    List<EsProduct> list=new ArrayList<>();
    if (!CollectionUtils.isEmpty(ids)){
      ids.forEach(t->{
        EsProduct e=new EsProduct();
        e.setId(t);
        list.add(e);
      });
    }
    esProductRepository.deleteAll(list);
  }

  @Override
  public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize) {
    Pageable pageable= PageRequest.of(pageNum,pageSize);
    if (StringUtils.isEmpty(keyword)) return esProductRepository.findAll(pageable);
    return esProductRepository.findByNameOrSubTitleOrKeywords(keyword,keyword,keyword,pageable);
  }

  @Override
  public Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize,
      Integer sort) {
    Pageable pageable=PageRequest.of(pageNum,pageSize);
    NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder();
    //分页
    nativeSearchQueryBuilder.withPageable(pageable);
    //过滤
    if (brandId !=null || productCategoryId!= null){
      BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
      if (brandId!=null){
        queryBuilder.must(QueryBuilders.termQuery("brandId",brandId));
      }
      if (productCategoryId!=null){
        queryBuilder.must(QueryBuilders.termQuery("productCategoryId",productCategoryId));
      }
      nativeSearchQueryBuilder.withFilter(queryBuilder);
    }
    if (StringUtils.isEmpty(keyword)){
      nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
    }else{
      List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders=new ArrayList<>();
      filterFunctionBuilders.add(new FilterFunctionBuilder(
          QueryBuilders.matchQuery("name",keyword),
          ScoreFunctionBuilders.weightFactorFunction(10)));
      filterFunctionBuilders.add(new FilterFunctionBuilder(
          QueryBuilders.matchQuery("subTitle",keyword),
          ScoreFunctionBuilders.weightFactorFunction(5)
      ));
      filterFunctionBuilders.add(new FilterFunctionBuilder(
          QueryBuilders.matchQuery("keywords",keyword),
          ScoreFunctionBuilders.weightFactorFunction(2)
      ));
      FilterFunctionBuilder[] builders=new FilterFunctionBuilder[filterFunctionBuilders.size()];
      filterFunctionBuilders.toArray(builders);
      FunctionScoreQueryBuilder functionScoreQueryBuilder=
          QueryBuilders.functionScoreQuery(
          builders)
          .scoreMode(ScoreMode.SUM)
          .setMinScore(2);
      nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
    }
    //排序
    if (sort==1){
      //按新品到新到旧
      nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
    }else if(sort==2){
      //按销量从高到低
      nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("sale").order(SortOrder.DESC));
    }else if(sort==3){
      //按价格从低到高
      nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));
    }else if (sort==4){
      //按价格从高到低
      nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
    }else{
      //相关度
      nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
    }
    nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
    NativeSearchQuery query=nativeSearchQueryBuilder.build();
    log.info("DSL: {}",query.getQuery().toString());
    return esProductRepository.search(query);
  }

  @Override
  public Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize) {
    Pageable pageable=PageRequest.of(pageNum,pageSize);
    List<EsProduct> list=esProductDao.getAllEsProductList(id);
    if (list.size()==0){
      return new PageImpl<>(null);
    }
    EsProduct esProduct = list.get(0);
    String keyword = esProduct.getName();
    Long brandId = esProduct.getBrandId();
    Long productCategoryId = esProduct.getProductCategoryId();

    //分页
    //nativeSearchQueryBuilder.withPageable(pageable);
      List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders=new ArrayList<>();
      filterFunctionBuilders.add(new FilterFunctionBuilder(
          QueryBuilders.matchQuery("name",keyword),
          ScoreFunctionBuilders.weightFactorFunction(8)));
      filterFunctionBuilders.add(new FilterFunctionBuilder(
          QueryBuilders.matchQuery("subTitle",keyword),
          ScoreFunctionBuilders.weightFactorFunction(2)
      ));
      filterFunctionBuilders.add(new FilterFunctionBuilder(
          QueryBuilders.matchQuery("keywords",keyword),
          ScoreFunctionBuilders.weightFactorFunction(2)
      ));
    filterFunctionBuilders.add(new FilterFunctionBuilder(
        QueryBuilders.matchQuery("branId",brandId),
        ScoreFunctionBuilders.weightFactorFunction(5)
    ));
    filterFunctionBuilders.add(new FilterFunctionBuilder(
        QueryBuilders.matchQuery("productCategoryId",productCategoryId),
        ScoreFunctionBuilders.weightFactorFunction(3)
    ));
      FilterFunctionBuilder[] builders=new FilterFunctionBuilder[filterFunctionBuilders.size()];
      filterFunctionBuilders.toArray(builders);
      FunctionScoreQueryBuilder functionScoreQueryBuilder=
          QueryBuilders.functionScoreQuery(
              builders)
              .scoreMode(ScoreMode.SUM)
              .setMinScore(2);
      //过滤
    BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
    boolQueryBuilder.mustNot(QueryBuilders.termQuery("id",id));
    //查询条件
    NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder();
    nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
    nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
    nativeSearchQueryBuilder.withPageable(pageable);
    NativeSearchQuery query=nativeSearchQueryBuilder.build();
    log.info("DSL: {}",query.getQuery().toString());
    return esProductRepository.search(query);
  }

  @Override
  public EsProductRelatedInfo searchRelatedInfo(String keyword) {
    NativeSearchQueryBuilder builder=new NativeSearchQueryBuilder();
    //搜索条件
    if(StringUtils.isEmpty(keyword)){
      builder.withQuery(QueryBuilders.matchAllQuery());
    }else{
      builder.withQuery(QueryBuilders.multiMatchQuery(keyword,"name","subTitle","keywords"));
    }
    //聚合搜索品牌名称
      builder.addAggregation(AggregationBuilders.terms("brandNames").field("brandName"));
    //集合搜索分类名称
      builder.addAggregation(AggregationBuilders.terms("productCategoryNames").field("productCategoryName"));
    //聚合搜索商品属性，去除type=1的属性
    AbstractAggregationBuilder aggregationBuilder = AggregationBuilders.nested("allAttrValues","attrValueList")
        .subAggregation(AggregationBuilders.filter("productAttrs",QueryBuilders.termQuery("attrValueList.type",1))
            .subAggregation(AggregationBuilders.terms("attrIds")
                .field("attrValueList.productAttributeId")
                .subAggregation(AggregationBuilders.terms("attrValues")
                    .field("attrValueList.value"))
                .subAggregation(AggregationBuilders.terms("attrNames")
                    .field("attrValueList.name"))));
    builder.addAggregation(aggregationBuilder);
    NativeSearchQuery searchQuery = builder.build();
    SearchHits<EsProduct> hits=elasticsearchTemplate.search(searchQuery,EsProduct.class);
    /*return elasticsearchTemplate.
        query(searchQuery,response -> {
      log.info("DSL:{}",searchQuery.getQuery().toString());
      return convertProductRelatedInfo(response);
    });*/
    return convertProductRelatedInfo(hits);
  }
  /**
   * 将返回结果转换为对象
   */
  private EsProductRelatedInfo convertProductRelatedInfo(SearchHits<EsProduct> response) {
    EsProductRelatedInfo productRelatedInfo = new EsProductRelatedInfo();
    Map<String, Aggregation> aggregationMap = response.getAggregations().getAsMap();
    //设置品牌
    Aggregation brandNames = aggregationMap.get("brandNames");
    List<String> brandNameList = new ArrayList<>();
    for(int i = 0; i<((Terms) brandNames).getBuckets().size(); i++){
      brandNameList.add(((Terms) brandNames).getBuckets().get(i).getKeyAsString());
    }
    productRelatedInfo.setBrandNames(brandNameList);
    //设置分类
    Aggregation productCategoryNames = aggregationMap.get("productCategoryNames");
    List<String> productCategoryNameList = new ArrayList<>();
    for(int i=0;i<((Terms) productCategoryNames).getBuckets().size();i++){
      productCategoryNameList.add(((Terms) productCategoryNames).getBuckets().get(i).getKeyAsString());
    }
    productRelatedInfo.setProductCategoryNames(productCategoryNameList);
    //设置参数
    Aggregation productAttrs = aggregationMap.get("allAttrValues");
    List<LongTerms.Bucket> attrIds = ((LongTerms) ((InternalFilter) ((InternalNested) productAttrs).getProperty("productAttrs")).getProperty("attrIds")).getBuckets();
    List<EsProductRelatedInfo.ProductAttr> attrList = new ArrayList<>();
    for (Terms.Bucket attrId : attrIds) {
      EsProductRelatedInfo.ProductAttr attr = new EsProductRelatedInfo.ProductAttr();
      attr.setAttrId((Long) attrId.getKey());
      List<String> attrValueList = new ArrayList<>();
      List<StringTerms.Bucket> attrValues = ((StringTerms) attrId.getAggregations().get("attrValues")).getBuckets();
      List<StringTerms.Bucket> attrNames = ((StringTerms) attrId.getAggregations().get("attrNames")).getBuckets();
      for (Terms.Bucket attrValue : attrValues) {
        attrValueList.add(attrValue.getKeyAsString());
      }
      attr.setAttrValues(attrValueList);
      if(!CollectionUtils.isEmpty(attrNames)){
        String attrName = attrNames.get(0).getKeyAsString();
        attr.setAttrName(attrName);
      }
      attrList.add(attr);
    }
    productRelatedInfo.setProductAttrs(attrList);
    return productRelatedInfo;
  }
}
