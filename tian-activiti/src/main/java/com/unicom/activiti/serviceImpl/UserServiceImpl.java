package com.unicom.activiti.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.activiti.service.IUserService;
import com.unicom.generator.entity.User;
import com.unicom.generator.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ctf
 * @since 2020-06-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {}
