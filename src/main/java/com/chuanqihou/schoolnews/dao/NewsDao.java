package com.chuanqihou.schoolnews.dao;


import com.chuanqihou.schoolnews.entity.News;
import com.chuanqihou.schoolnews.vo.NewsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther 传奇后
 * @date 2022/11/8 15:16
 * @veersion 1.0
 */
public interface NewsDao {

    List<NewsVo> selectPosition(Integer position);

    List<NewsVo> queryIndexNews(Long categorizeId);

    NewsVo selectNewsByNewsId(Long newsId);

    List<NewsVo> queryNewsByCategorize(Map<String, Object> map);

    List<NewsVo> queryNewsByRecent();


    Integer queryNewsByCategorizeTotal(Long categoryId);

    List<NewsVo> page(Map<String, Object> map);

    Integer pageTotal(@Param("categoryId") Long categoryId, @Param("title") String title);

    int delete(News news);

    int add(News news);

    int update(News news);

    News getNewsByPosition(Integer position);
}
