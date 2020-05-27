package com.unicom.generator.mapper;

import com.unicom.generator.entity.UmsAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ctf
 * @since 2020-05-07
 */
public interface UmsAdminMapper extends BaseMapper<UmsAdmin> {
    public List<Map<String,Object>> selectByColumn(List<String> ips);

    public List<Object> selectByColumnObject(List<String> ips);



}
