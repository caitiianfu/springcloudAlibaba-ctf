package com.unicom.search.controller;/**
 *
 */

import com.unicom.common.api.ResultUtils;
import com.unicom.search.entity.EsProduct;
import com.unicom.search.service.EsProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description TODO
 * @author ctf
 * @date 2020/7/20
 */
@RestController
@RequestMapping("/product")
public class ProductController {
  @Autowired
  private EsProductService esProductService;
  @PostMapping("/importAll")
  public ResultUtils importAll(){
    return ResultUtils.success(esProductService.importAll());
  }
  @PostMapping("/delete/{id}")
  public ResultUtils delete(@PathVariable Long id){
    esProductService.delete(id);
    return ResultUtils.success(null);
  }
  @PostMapping("/create/{id}")
  public ResultUtils create(@PathVariable Long id){
    return ResultUtils.success(esProductService.create(id));
  }
  @PostMapping("/deleteAll")
  public ResultUtils deleteAll(List<Long> ids){
    esProductService.deleteAll(ids);
    return ResultUtils.success(null);
  }
  @GetMapping("/search")
  public ResultUtils search(String keyword,
      @RequestParam(value = "pageNum",defaultValue = "0")Integer pageNum,
      @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
    Page<EsProduct> page= esProductService.search(keyword,pageNum,pageSize);
    return ResultUtils.success(page);
  }
  /**综合查询**/
  @GetMapping("/searchComplex")
  public ResultUtils searchComplex(String keyword,
      @RequestParam(value = "pageNum",defaultValue = "0")Integer pageNum,
      @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize
  ,Long brandId, Long productCategoryId,Integer sort
  ){

    Page<EsProduct> page= esProductService.search(keyword,brandId,productCategoryId,pageNum,pageSize,sort);
    return ResultUtils.success(page);
  }

  /**推荐查询**/
  @GetMapping("/searchRecommand")
  public ResultUtils searchRecommand(
      @RequestParam(value = "pageNum",defaultValue = "0")Integer pageNum,
      @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize
      ,Long id
  ){

    Page<EsProduct> page= esProductService.recommend(id,pageNum,pageSize);
    return ResultUtils.success(page);
  }


  /**聚合查询**/
  @GetMapping("/searchRelatedInfo")
  public ResultUtils searchRelatedInfo(
   String keyword
  ){
    return ResultUtils.success(esProductService.searchRelatedInfo(keyword));
  }
}

