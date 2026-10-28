package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.BusinessException;
import com.forum.common.UserContext;
import com.forum.dto.CreatePostDTO;
import com.forum.dto.PostVO;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.mapper.PostMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;   // 联表查作者名用

    @Override
    public void create(CreatePostDTO dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthorId(UserContext.getUserId());   // 从令牌取当前用户 ID
        postMapper.insert(post);
    }

    @Override
    public PostVO getById(Long id) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        return toVO(post);
    }

    @Override
    public List<PostVO> getList(int page, int size) {
        Page<Post> pageParam = new Page<>(page, size);
        Page<Post> result = postMapper.selectPage(pageParam,
                new QueryWrapper<Post>().orderByDesc("created_at"));  // 最新在前
        return result.getRecords().stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public void delete(Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        if (!post.getAuthorId().equals(UserContext.getUserId())) {
            throw new BusinessException("只能删除自己的帖子");
        }
        postMapper.deleteById(postId);
    }

    // 把 Post 实体 → PostVO（补上作者名）
    private PostVO toVO(Post post) {
        PostVO vo = new PostVO();
        vo.setId(post.getId());
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setAuthorId(post.getAuthorId());
        vo.setCreatedAt(post.getCreatedAt());
        vo.setUpdatedAt(post.getUpdatedAt());

        User author = userMapper.selectById(post.getAuthorId());
        vo.setAuthorName(author != null ? author.getUsername() : "未知用户");
        return vo;
    }
}