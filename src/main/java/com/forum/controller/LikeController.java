package com.forum.controller;

import com.forum.common.Result;
import com.forum.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // 点赞
    @PostMapping("/{postId}")
    public Result like(@PathVariable Long postId) {
        likeService.like(postId);
        return Result.success();
    }

    // 取消赞
    @DeleteMapping("/{postId}")
    public Result unlike(@PathVariable Long postId) {
        likeService.unlike(postId);
        return Result.success();
    }

    // 查点赞状态 + 赞数
    @GetMapping("/{postId}")
    public Result status(@PathVariable Long postId) {
        boolean liked = likeService.isLiked(postId);
        Long count = likeService.getLikeCount(postId);
        return Result.success(Map.of("liked", liked, "count", count));
    }

    // 热榜（游客可看）
    @GetMapping("/hot")
    public Result hot(@RequestParam(defaultValue = "10") int topN) {
        List<Map<String, Object>> list = likeService.getHotPosts(topN);
        return Result.success(list);
    }
}