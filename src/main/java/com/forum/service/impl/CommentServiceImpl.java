package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.BusinessException;
import com.forum.common.UserContext;
import com.forum.dto.CommentVO;
import com.forum.dto.CreateCommentDTO;
import com.forum.entity.Comment;
import com.forum.entity.User;
import com.forum.mapper.CommentMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void create(CreateCommentDTO dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setPostId(dto.getPostId());
        comment.setUserId(UserContext.getUserId());
        comment.setParentId(dto.getParentId());
        commentMapper.insert(comment);
    }

    @Override
    public List<CommentVO> getByPostId(Long postId, int page, int size) {
        Page<Comment> pageParam = new Page<>(page, size);
        Page<Comment> result = commentMapper.selectPage(pageParam,
                new QueryWrapper<Comment>()
                        .eq("post_id", postId)
                        .isNull("parent_id")        // 只查一级评论
                        .orderByDesc("created_at"));
        return result.getRecords().stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public void delete(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        if (!comment.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException("只能删除自己的评论");
        }
        commentMapper.deleteById(commentId);
    }

    private CommentVO toVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setPostId(comment.getPostId());
        vo.setUserId(comment.getUserId());
        vo.setParentId(comment.getParentId());
        vo.setCreatedAt(comment.getCreatedAt());

        User author = userMapper.selectById(comment.getUserId());
        vo.setAuthorName(author != null ? author.getUsername() : "未知用户");
        return vo;
    }
}