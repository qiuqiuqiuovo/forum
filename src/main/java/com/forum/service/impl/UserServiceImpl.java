package com.forum.service.impl;

import com.forum.entity.User;
import com.forum.mapper.UserMapper;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;    // 不用 new！Spring 塞进来

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);   // 活还是 Mapper 干，Service 负责调度
    }
}