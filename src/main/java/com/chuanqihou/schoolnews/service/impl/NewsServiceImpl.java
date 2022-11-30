package com.chuanqihou.schoolnews.service.impl;

import com.chuanqihou.schoolnews.dao.NewsDao;
import com.chuanqihou.schoolnews.dao.NewsImgDao;
import com.chuanqihou.schoolnews.entity.NesImg;
import com.chuanqihou.schoolnews.entity.News;
import com.chuanqihou.schoolnews.service.NewsService;
import com.chuanqihou.schoolnews.utils.SnowflakeManager;
import com.chuanqihou.schoolnews.vo.NewsVo;
import com.chuanqihou.schoolnews.vo.ResultVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @auther 传奇后
 * @date 2022/11/8 15:58
 * @veersion 1.0
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsDao newsDao;

    @Resource
    private NewsImgDao newsImgDao;

    @Resource
    private SnowflakeManager snowflakeManager;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据条件查询新闻信息（首页新闻数据）
     * @param position 条件：新闻位置（0：普通位置 1：新闻置顶 2：大横幅 3：小横幅）
     * @return
     */
    @Override
    public ResultVo queryBigNews(Integer position) {
        List<NewsVo> newsVoList = null;
        //从redis中查询
        newsVoList = (List<NewsVo>)redisTemplate.opsForValue().get(Integer.toString(position));
        //没有数据再从数据库中查询
        if (newsVoList == null) {
            //查询数据库
            newsVoList = newsDao.selectPosition(position);
            //缓存到redis
            redisTemplate.opsForValue().set(Integer.toString(position),newsVoList);
        }
        return new ResultVo(1001, "success", newsVoList);
    }

    /**
     * 根据新闻分类id查询分类信息
     * @param categorizeId 分类id
     * @return
     */
    @Override
    public ResultVo queryIndexNews(Long categorizeId) {
        List<NewsVo> newsVoList = null;
        newsVoList = (List<NewsVo>)redisTemplate.opsForValue().get(Long.toString(categorizeId));
        if (newsVoList == null) {
            newsVoList = newsDao.queryIndexNews(categorizeId);
            //缓存到redis
            redisTemplate.opsForValue().set(Long.toString(categorizeId),newsVoList);
        }
        return new ResultVo(1001,"success",newsVoList);
    }

    /**
     * 根据新闻id查询新闻详细信息
     * @param newsId 新闻id
     * @return
     */
    @Override
    public ResultVo selectNewsByNewsId(Long newsId) {
        NewsVo newsVo = null;
        newsVo = (NewsVo)redisTemplate.opsForValue().get(Long.toString(newsId));
        if (newsVo == null) {
            newsVo = newsDao.selectNewsByNewsId(newsId);
            //缓存到redis
            redisTemplate.opsForValue().set(Long.toString(newsId),newsVo);
        }
        if (newsVo != null) {
            return new ResultVo(1001,"success",newsVo);
        }
        return new ResultVo(1002,"fail",null);
    }

    /**
     * 根据新闻分类id 分页查询新闻信息
     * @param categoryId 新闻分类id
     * @param skipCount 起始数（从第几条数据开始查）
     * @param pageSize 每页数据条数
     * @return
     */
    @Override
    public ResultVo queryNewsByCategorize(Long categoryId,Integer skipCount,Integer pageSize) {
        List<NewsVo> newsVos2 = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);
        String key = categoryId+"."+skipCount+"."+pageSize;
        List<NewsVo> newsVoList = null;
        Integer total = (Integer) redisTemplate.opsForValue().get(key+"total");
        newsVoList = (List<NewsVo>)redisTemplate.opsForValue().get(key);
        if (newsVoList == null) {
            newsVoList = newsDao.queryNewsByCategorize(map);
            total = newsDao.queryNewsByCategorizeTotal(categoryId);
            //缓存到redis
            redisTemplate.opsForValue().set(key,newsVoList);
            redisTemplate.opsForValue().set(key+"total",total);
        }
        for (int i = 0; i < newsVoList.size(); i++) {
            NewsVo newsVo = newsVoList.get(i);
            newsVo.setYears(newsVo.getCreateTime().substring(0,7));
            newsVo.setDay(newsVo.getCreateTime().substring(8,10));
            newsVos2.add(newsVo);
        }
        if (newsVoList.size()<=0) {
            return new ResultVo(1002,"fail",null);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("newsVo", newsVos2);
        resultMap.put("total",total);
        return new ResultVo(1001,"success",resultMap);
    }

    /**
     * 查询最近更新的五条新闻
     * @return
     */
    @Override
    public ResultVo queryNewsByRecent() {
        List<NewsVo> newsVoList = null;
        newsVoList = (List<NewsVo>)redisTemplate.opsForValue().get("recent");
        if (newsVoList == null) {
            newsVoList = newsDao.queryNewsByRecent();
            //缓存到redis
            redisTemplate.opsForValue().set("recent",newsVoList);
        }
        return new ResultVo(1001,"success",newsVoList);
    }

    /**
     * 根据条件分页查询新闻
     * @param categoryId 新闻分类id
     * @param title 新闻标题
     * @param skipCount 起始数（从第几条数据开始查）
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public ResultVo page(Long categoryId, String title, Integer skipCount, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);
        map.put("title", title);
        List<NewsVo> newsVos = newsDao.page(map);
        Integer total = newsDao.pageTotal(categoryId,title);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("newsVo", newsVos);
        resultMap.put("total",total);
        return new ResultVo(1001,"success",resultMap);
    }

    /**
     * 删除新闻
     * @param news 新闻信息
     * @return
     */
    @Override
    public ResultVo deleteById(News news) {
        //清除redis中所有数据
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);

        int i = newsDao.delete(news);
        if (i > 0) {
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }

    /**
     * 添加新闻
     * @param newsVo 信息信息
     * @return
     */
    @Override
    public ResultVo add(NewsVo newsVo) {
        //清除redis中所有数据
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);

        News news = new News();
        try {
            Long newsId = Long.valueOf(String.valueOf(snowflakeManager.nextValue()).substring(0, 16));
            news.setId(newsId);
            news.setCreateTime(new Date());
            news.setNewsTitle(newsVo.getNewsTitle());
            news.setNewsContent(newsVo.getNewsContent());
            news.setPosition(newsVo.getPosition());
            news.setCategorizeId(newsVo.getCategorizeId());
            news.setSource(newsVo.getSource());
            news.setCreateUserId(newsVo.getCreateUserId());
            News n = newsDao.getNewsByPosition(newsVo.getPosition());
            n.setPosition(0);
            newsDao.update(n);
            if (newsVo.getImgs().length!=0 && newsVo.getImgs()[0].endsWith(".mp4")){
                news.setVideo(newsVo.getImgs()[0]);
            }else {
                String[] imgs = newsVo.getImgs();
                if (newsVo.getImgs().length!=0) {
                    for (int i = 0; i < imgs.length; i++) {
                        NesImg nesImg = new NesImg();
                        nesImg.setId(Long.valueOf(String.valueOf(snowflakeManager.nextValue()).substring(0, 16)));
                        nesImg.setNewsId(newsId);
                        nesImg.setCreateTime(new Date());
                        nesImg.setUrl(imgs[i]);
                        int result = newsImgDao.add(nesImg);
                    }
                }
            }
            int i = newsDao.add(news);
            if (i>0){
                return new ResultVo(1001,"success",news);
            }
            return new ResultVo(1002,"fail",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新新闻
     * @param newsVo 新闻信息
     * @return
     */
    @Override
    public ResultVo update(NewsVo newsVo) {
        //清除redis中所有数据
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);

        News news = new News();
        try {
            news.setId(newsVo.getId());
            news.setNewsTitle(newsVo.getNewsTitle());
            news.setNewsContent(newsVo.getNewsContent());
            news.setPosition(newsVo.getPosition());
            news.setCategorizeId(newsVo.getCategorizeId());
            news.setSource(newsVo.getSource());
            news.setUpdateUserId(newsVo.getCreateUserId());
            news.setUpdateTime(new Date());
            News n = newsDao.getNewsByPosition(newsVo.getPosition());
            n.setPosition(0);
            newsDao.update(n);
            newsImgDao.delete(newsVo.getId());
            if (newsVo.getImgs().length!=0) {
                String[] imgs = newsVo.getImgs();
                for (int i = 0; i < imgs.length; i++) {
                    if(imgs[i].endsWith(".mp4")){
                        news.setVideo(newsVo.getImgs()[0]);
                    }else{
                        NesImg nesImg = new NesImg();
                        nesImg.setId(Long.valueOf(String.valueOf(snowflakeManager.nextValue()).substring(0, 16)));
                        nesImg.setNewsId(newsVo.getId());
                        nesImg.setCreateTime(new Date());
                        nesImg.setUrl(imgs[i]);
                        int result = newsImgDao.add(nesImg);
                    }
                }
            }
            int i = newsDao.update(news);
            if (i>0){
                return new ResultVo(1001,"success",news);
            }
            return new ResultVo(1002,"fail",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
