package com.chuanqihou.schoolnews.service.impl;


import com.chuanqihou.schoolnews.dao.CommentDao;
import com.chuanqihou.schoolnews.entity.Comment;
import com.chuanqihou.schoolnews.service.CommentService;
import com.chuanqihou.schoolnews.utils.SnowflakeManager;
import com.chuanqihou.schoolnews.vo.CommentVo;
import com.chuanqihou.schoolnews.vo.NewsVo;
import com.chuanqihou.schoolnews.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * @auther 传奇后
 * @date 2022/11/8 17:08
 * @veersion 1.0
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Resource
    private SnowflakeManager snowflakeManager;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据新闻id查询评论信息
     * @param newsId 新闻id
     * @return
     */
    @Override
    public ResultVo queryCommentByNewsId(Long newsId) {

        String key = newsId+"comment";
        List<CommentVo> comments = null;
        comments = (List<CommentVo>)redisTemplate.opsForValue().get(key);
        if (comments == null) {
            System.out.println("111查数据库！");
            comments = commentDao.queryCommentByNewsId(newsId);
            //缓存到redis
            redisTemplate.opsForValue().set(key,comments);
        }
        return new ResultVo(1001, "success", comments);
    }

    /**
     * 添加评论
     * @param comment 评论信息
     * @return
     */
    @Override
    public ResultVo insertByNewsIdAndUserId(Comment comment) {
        try {
            //清除redis中所有数据
            Set<String> keys = redisTemplate.keys("*");
            redisTemplate.delete(keys);

            comment.setCreateTime(new Date());
            comment.setId(Long.valueOf(String.valueOf(snowflakeManager.nextValue()).substring(0,16)));
            comment.setIsDelete(0);
            int i = commentDao.insertByNewsIdAndUserId(comment);
            if (i > 0) {
                return new ResultVo(1001, "success", i);
            }
            return new ResultVo(1002, "失败", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据条件分页查询所有评论信息
     * @param status 条件：评论状态
     * @param commentDetail 条件：评论内容
     * @param commentUser 条件：评论用户
     * @param skipCount 起始数（从第几条数据开始查）
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public ResultVo page(Integer status, String commentDetail, String commentUser, Integer skipCount, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("commentDetail", commentDetail);
        map.put("commentUser", commentUser);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);
        List<CommentVo> comments = commentDao.page(status,commentDetail,commentUser,skipCount,pageSize);
        Integer total = commentDao.pageTotal(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("comments", comments);
        resultMap.put("total",total);
        return new ResultVo(1001,"success",resultMap);
    }

    /**
     * 删除评论
     * @param comment 评论信息
     * @return
     */
    @Override
    public ResultVo deleteComment(Comment comment) {
        //清除redis中所有数据
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);

        int i = commentDao.delete(comment);
        if (i > 0) {
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }

    /**
     * 更改评论状态
     * @param comment 评论信息
     * @return
     */
    @Override
    public ResultVo updateStatus(Comment comment) {
        //清除redis中所有数据
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);

        int j = commentDao.updateStatus(comment);
        if (j > 0) {
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }

    /**
     * 查询所有评论信息（导出评论数据）
     * @return
     */
    @Override
    public List<CommentVo> queryAllComments() {
        return commentDao.queryAllComments();
    }
}
