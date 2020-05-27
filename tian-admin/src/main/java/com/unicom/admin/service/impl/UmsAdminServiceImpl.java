package com.unicom.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.admin.bo.AdminUserDetails;
import com.unicom.admin.service.IUmsAdminService;
import com.unicom.admin.service.IUmsResourceService;
import com.unicom.common.exception.Assert;
import com.unicom.generator.entity.UmsAdmin;
import com.unicom.generator.entity.UmsResource;
import com.unicom.generator.mapper.UmsAdminMapper;
import com.unicom.generator.mapper.UmsResourceMapper;
import com.unicom.security.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ctf
 * @since 2020-05-07
 */
@Slf4j
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements IUmsAdminService {
    @Autowired
    private UmsAdminMapper umsAdminMapper;
    @Autowired
    private IUmsResourceService iUmsResourceService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public UmsAdmin getAdminByUsername(String username) {
        QueryWrapper<UmsAdmin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<UmsAdmin> adminList=umsAdminMapper.selectList(queryWrapper);
        if (adminList!=null&&adminList.size()>0){
            return  adminList.get(0);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
       //获取用户信息
        UmsAdmin umsAdmin=getAdminByUsername(username);
       if (umsAdmin!=null){
           //获取资源信息
           List<UmsResource> resources=iUmsResourceService.getResourceList(umsAdmin.getId());
            return new AdminUserDetails(umsAdmin,resources);
       }
       //该异常会在EntryPoint进行返回
       throw  new UsernameNotFoundException("用户名"+username+"不存在");
    }

    @Override
    public String login(String username, String password) {
        String token=null;
        /*try {*/
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码错误");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtTokenUtil.generateToken(userDetails);
       /* }catch (AuthenticationException e){
            log.warn("登录异常 {}",e.getMessage());
        }*/
        return token;
    }

    @Override
    public List<Map<String,Object>> selectByColumn(List<String> ips) {
        Field f;
        for (String s:
                ips) {
            Class clazz=UmsAdmin.class;
            try {
                clazz.getDeclaredField(s);
            } catch (NoSuchFieldException e) {
               // e.printStackTrace();
                System.out.println("cccw");

                Assert.fail("传参错误");

            }
        }
        return umsAdminMapper.selectByColumn(ips);
    }

    @Override
    public List<Object> selectByColumnObject(List<String> ips) {
        Field f;
        for (String s:
             ips) {
           Class clazz=UmsAdmin.class;
            try {
                clazz.getDeclaredField(s);
            } catch (NoSuchFieldException e) {
                //e.printStackTrace();
                System.out.println("cccw");
                Assert.fail("传参错误");
            }
        }
        System.out.println("wwww");

        return umsAdminMapper.selectByColumnObject(ips);
    }
}
