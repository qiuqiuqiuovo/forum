package com.forum.service;

import java.util.List;
import java.util.Map;

public interface LikeService {
    void like(Long postId);          // 点赞
    void unlike(Long postId);        // 取消赞
    boolean isLiked(Long postId);    // 当前用户是否已赞
    Long getLikeCount(Long postId);  // 某帖子点赞数
    List<Map<String, Object>> getHotPosts(int topN);  // 热榜 topN
}