package com.chuanqihou.schoolnews.service;


import com.chuanqihou.schoolnews.entity.Comment;
import com.chuanqihou.schoolnews.vo.CommentVo;
import com.chuanqihou.schoolnews.vo.ResultVo;

import java.util.List;

/**
 * @auther 传奇后
 * @date 2022/11/8 15:18
 * @veersion 1.0
 */
public interface CommentService {
    ResultVo queryCommentByNewsId(Long newsId);

    ResultVo insertByNewsIdAndUserId(Comment comment);

    ResultVo page(Integer status, String commentDetail, String commentUser, Integer skipCount, Integer pageSize);

    ResultVo deleteComment(Comment comment);

    ResultVo updateStatus(Comment comment);

    List<CommentVo> queryAllComments();

}
