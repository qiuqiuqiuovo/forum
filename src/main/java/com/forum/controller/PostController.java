package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.CreatePostDTO;
import com.forum.dto.PostVO;
import com.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    // 发帖（需要登录）
    @PostMapping
    public Result create(@RequestBody CreatePostDTO dto) {
        postService.create(dto);
        return Result.success();
    }

    // 帖子详情（游客可看）
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        PostVO vo = postService.getById(id);
        return Result.success(vo);
    }

    // 帖子列表（游客可看）→ /api/post?page=1&size=10
    @GetMapping
    public Result getList(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        List<PostVO> list = postService.getList(page, size);
        return Result.success(list);
    }

    // 删帖（只能删自己的）
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        postService.delete(id);
        return Result.success();
    }
}