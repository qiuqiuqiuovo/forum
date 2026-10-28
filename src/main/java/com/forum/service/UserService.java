package com.forum.service;

import com.forum.dto.LoginDTO;
import com.forum.entity.User;

public interface UserService {
    User getById(Long id);   // 合同：提供"按 id 查用户"的能力
    void register(User user);
    String login(LoginDTO loginDTO);
}