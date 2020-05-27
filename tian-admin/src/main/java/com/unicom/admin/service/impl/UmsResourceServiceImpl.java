package com.unicom.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.admin.service.IUmsResourceService;
import com.unicom.generator.entity.UmsResource;
import com.unicom.generator.mapper.UmsResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ctf
 * @since 2020-05-07
 */
@Service
public class UmsResourceServiceImpl extends ServiceImpl<UmsResourceMapper, UmsResource> implements IUmsResourceService {
    @Autowired
    private UmsResourceMapper umsResourceMapper;
    @Override
    public List<UmsResource> listAll() {
        return umsResourceMapper.selectList(null);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        return umsResourceMapper.getResourceList(adminId);
    }
}
