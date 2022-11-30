package com.chuanqihou.schoolnews.service;


import com.chuanqihou.schoolnews.entity.News;
import com.chuanqihou.schoolnews.vo.NewsVo;
import com.chuanqihou.schoolnews.vo.ResultVo;

/**
 * @auther 传奇后
 * @date 2022/11/8 15:17
 * @veersion 1.0
 */
public interface NewsService {
    ResultVo queryBigNews(Integer position);

    ResultVo queryIndexNews(Long categorizeId);

    ResultVo selectNewsByNewsId(Long newsId);

    ResultVo queryNewsByCategorize(Long categoryId,Integer skipCount,Integer pageSize);

    ResultVo queryNewsByRecent();

    ResultVo page(Long categoryId, String title, Integer skipCount, Integer pageSize);

    ResultVo deleteById(News news);

    ResultVo add(NewsVo newVo);

    ResultVo update(NewsVo newsVo);
}
