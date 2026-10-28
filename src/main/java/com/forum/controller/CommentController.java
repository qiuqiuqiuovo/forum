package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.CommentVO;
import com.forum.dto.CreateCommentDTO;
import com.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 发评论（需登录）
    @PostMapping
    public Result create(@RequestBody CreateCommentDTO dto) {
        commentService.create(dto);
        return Result.success();
    }

    // 查某帖子的评论（游客可看）
    @GetMapping
    public Result getByPostId(@RequestParam Long postId,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size) {
        List<CommentVO> list = commentService.getByPostId(postId, page, size);
        return Result.success(list);
    }

    // 删评论（只能删自己的）
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.success();
    }
}