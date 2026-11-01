package com.forum.service.impl;

import com.forum.common.BusinessException;
import com.forum.common.UserContext;
import com.forum.mapper.PostMapper;
import com.forum.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LikeServiceImpl implements LikeService {

    private static final String LIKES_KEY = "post:likes:";
    private static final String RANKING_KEY = "post:ranking";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PostMapper postMapper;

    @Override
    public void like(Long postId) {
        if (postMapper.selectById(postId) == null) {
            throw new BusinessException("帖子不存在");
        }
        String userId = UserContext.getUserId().toString();
        redisTemplate.opsForSet().add(LIKES_KEY + postId, userId);
        redisTemplate.opsForZSet().incrementScore(RANKING_KEY, postId.toString(), 1);
    }

    @Override
    public void unlike(Long postId) {
        String userId = UserContext.getUserId().toString();
        redisTemplate.opsForSet().remove(LIKES_KEY + postId, userId);
        redisTemplate.opsForZSet().incrementScore(RANKING_KEY, postId.toString(), -1);
    }

    @Override
    public boolean isLiked(Long postId) {
        String userId = UserContext.getUserId().toString();
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(LIKES_KEY + postId, userId));
    }

    @Override
    public Long getLikeCount(Long postId) {
        return redisTemplate.opsForSet().size(LIKES_KEY + postId);
    }

    @Override
    public List<Map<String, Object>> getHotPosts(int topN) {
        Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<String>> tuples =
                redisTemplate.opsForZSet()
                        .reverseRangeWithScores(RANKING_KEY, 0, topN - 1);

        if (tuples == null) return Collections.emptyList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (var t : tuples) {
            Map<String, Object> map = new HashMap<>();
            map.put("postId", Long.valueOf(t.getValue()));
            map.put("likeCount", t.getScore().longValue());
            result.add(map);
        }
        return result;
    }
}