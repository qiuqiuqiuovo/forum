package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.common.BusinessException;
import com.forum.dto.LoginDTO;
import com.forum.entity.User;
import com.forum.mapper.UserMapper;
import com.forum.service.UserService;
import com.forum.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;    // 不用 new！Spring 塞进来
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);   // 活还是 Mapper 干，Service 负责调度
    }

    @Override
    public void register(User user) {
        // ① 查用户名是否已存在
        Long count = userMapper.selectCount(
                new QueryWrapper<User>().eq("username", user.getUsername())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        // ② 密码哈希后再存
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ③ 插入数据库
        userMapper.insert(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {

        // 1. 查出用户
        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("username", loginDTO.getUsername())
        );
        if (user == null) {
            throw new BusinessException("用户名不存在");
        }

        // 2. 验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 3. 签发令牌
        return JwtUtil.generateToken(user.getId(), user.getUsername());
    }
}
