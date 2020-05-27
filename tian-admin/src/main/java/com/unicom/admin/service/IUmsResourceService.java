package com.unicom.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unicom.generator.entity.UmsResource;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ctf
 * @since 2020-05-07
 */
public interface IUmsResourceService extends IService<UmsResource> {
    /**
     * 获得所有资源
     * @return
     */
    public List<UmsResource> listAll();
    /**
     * 获得用户资源
     */
    public List<UmsResource> getResourceList(Long adminId);
}
