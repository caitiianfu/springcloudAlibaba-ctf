package com.unicom.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unicom.generator.entity.UmsAdmin;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ctf
 * @since 2020-05-07
 */
public interface IUmsAdminService extends IService<UmsAdmin> {
    /**
     * 根据用户名获得用户
     */
    UmsAdmin getAdminByUsername(String username);
    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);
    /**
     * 登录获得token
     */
    String login(String username,String password);

    List<Map<String,Object>> selectByColumn(List<String> ips);


     List<Object> selectByColumnObject(List<String> ips);

}
