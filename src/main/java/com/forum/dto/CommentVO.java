package com.forum.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private String authorName;
    private Long parentId;
    private LocalDateTime createdAt;
}