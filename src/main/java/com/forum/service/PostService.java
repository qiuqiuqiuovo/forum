package com.forum.service;

import com.forum.dto.CreatePostDTO;
import com.forum.dto.PostVO;

import java.util.List;

public interface PostService {
    void create(CreatePostDTO dto);
    PostVO getById(Long id);
    List<PostVO> getList(int page, int size);
    void delete(Long postId);
}