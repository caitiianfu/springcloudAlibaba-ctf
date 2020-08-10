package com.unicom.search.service;/**
 *
 **/

import com.unicom.search.entity.EsProduct;
import com.unicom.search.entity.EsProductRelatedInfo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;

/**
 * @Description TODO
 * @date 2020/7/20
 * @author ctf
 **/
public interface EsProductService {
    /**
     *
     * @Description 从数据库导入商品到es
     * @param
     * @return int
     * @date 2020/7/20
     * @author ctf
     **/

    int importAll();
    /**
     *
     * @Description 删除商品
     * @param id
     * @return void
     * @date 2020/7/20
     * @author ctf
     **/

    void delete(Long id);
    /**
     *
     * @Description 根据商品id创建商品
     * @param id
     * @return com.unicom.search.entity.EsProduct
     * @date 2020/7/20
     * @author ctf
     **/
    EsProduct create(Long id);

    /**
     *
     * @Description 批量删除商品
     * @param ids
     * @return void
     * @date 2020/7/20
     * @author ctf
     **/
    void deleteAll(List<Long> ids);
    /**
     *
     * @Description 根据关键字搜索名称或者副标题
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return org.springframework.data.domain.Page<com.unicom.search.entity.EsProduct>
     * @date 2020/7/20
     * @author ctf
     **/

    Page<EsProduct> search(String keyword,Integer pageNum,Integer pageSize);

    /**
     *
     * @Description 按商品比重综合查询
     * @param keyword 关键字
     * @param brandId
     * @param productCategoryId
     * @param pageNum
     * @param pageSize
     * @param sort
     * @return org.springframework.data.domain.Page<com.unicom.search.entity.EsProduct>
     * @date 2020/7/21
     * @author ctf
     **/

    Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize,
        Integer sort);

    /**
     *
     * @Description 推荐查询
     * @param id 排除id
     * @param pageNum
     * @param pageSize
     * @return org.springframework.data.domain.Page<com.unicom.search.entity.EsProduct>
     * @date 2020/7/21
     * @author ctf
     **/

    Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize);

     EsProductRelatedInfo searchRelatedInfo(String keyword);

    }
