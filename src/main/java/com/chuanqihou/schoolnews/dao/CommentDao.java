package com.chuanqihou.schoolnews.dao;


import com.chuanqihou.schoolnews.entity.Comment;
import com.chuanqihou.schoolnews.vo.CommentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther 传奇后
 * @date 2022/11/8 15:16
 * @veersion 1.0
 */
public interface CommentDao {
    List<CommentVo> queryCommentByNewsId(Long newsId);

    int insertByNewsIdAndUserId(Comment comment);

    //List<CommentVo> page(Map<String, Object> map);
    List<CommentVo> page(@Param("status") Integer status, @Param("commentDetail") String commentDetail, @Param("commentUser") String commentUser, @Param("skipCount") Integer skipCount, @Param("pageSize") Integer pageSize);
    Integer pageTotal(Map<String, Object> map);

    int delete(Comment comment);

    int updateStatus(Comment comment);

    List<CommentVo> queryAllComments();

}
