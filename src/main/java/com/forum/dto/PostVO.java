package com.forum.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostVO {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;      // ← 联表查来的
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}