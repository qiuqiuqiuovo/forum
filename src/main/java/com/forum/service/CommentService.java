package com.forum.service;

import com.forum.dto.CommentVO;
import com.forum.dto.CreateCommentDTO;

import java.util.List;

public interface CommentService {
    void create(CreateCommentDTO dto);
    List<CommentVO> getByPostId(Long postId, int page, int size);
    void delete(Long commentId);
}