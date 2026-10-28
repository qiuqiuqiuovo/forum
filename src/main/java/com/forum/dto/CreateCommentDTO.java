package com.forum.dto;

import lombok.Data;

@Data
public class CreateCommentDTO {
    private Long postId;
    private String content;
    private Long parentId;   // 回复某条评论时才传，一级评论不传
}