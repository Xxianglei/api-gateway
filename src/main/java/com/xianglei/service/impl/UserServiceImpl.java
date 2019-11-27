package com.xianglei.service.impl;

import com.xianglei.common.Tools;
import com.xianglei.domain.User;
import com.xianglei.mapper.UserMapper;
import com.xianglei.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser(User user) {
        User normalUser = userMapper.getUserFromNomal(user);
        return normalUser;
    }
}
