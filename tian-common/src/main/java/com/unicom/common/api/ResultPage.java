package com.unicom.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import java.util.List;
@Data
public class ResultPage<T> {
    private long pageNum;
    private long pageSize;
    private long totalPage;
    private long total;
    private List<T> list;


    public static <T> ResultPage<T> restPage(IPage<T> pageInfo){
        ResultPage<T> resultPage=new ResultPage<>();
        resultPage.setPageNum(pageInfo.getCurrent());
        resultPage.setPageSize(pageInfo.getSize());
        resultPage.setTotalPage(pageInfo.getPages());
        resultPage.setTotal(pageInfo.getTotal());
        resultPage.setList(pageInfo.getRecords());
        return resultPage;

    }
    /**
     * 将pageHelper分页后的list转换为统一的返回类
     */
   /* public static <T> ResultPage<T> restPage(List<T> list){
        ResultPage<T> resultPage=new ResultPage<>();
        PageInfo<T> pageInfo=new PageInfo<>(list);
        resultPage.setPageNum(pageInfo.getPageNum());
        resultPage.setPageSize(pageInfo.getPageSize());
        resultPage.setTotalPage(pageInfo.getPages());
        resultPage.setTotal(pageInfo.getTotal());
        resultPage.setList(pageInfo.getList());
        return resultPage;

    }*/

    /**
     * 将SpringData分页后的list转换为统一的返回类
     */
   /* public static <T> ResultPage<T> restPage(Page<T> pageInfo){
        ResultPage<T> resultPage=new ResultPage<>();
        resultPage.setPageNum(pageInfo.getNumber());
        resultPage.setPageSize(pageInfo.getSize());
        resultPage.setTotalPage(pageInfo.getTotalPages());
        resultPage.setTotal(pageInfo.getTotalElements());
        resultPage.setList(pageInfo.getContent());
        return resultPage;

    }*/

}
