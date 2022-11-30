package com.chuanqihou.schoolnews.controller;

import com.chuanqihou.schoolnews.entity.News;
import com.chuanqihou.schoolnews.service.NewsService;
import com.chuanqihou.schoolnews.vo.NewsVo;
import com.chuanqihou.schoolnews.vo.ResultVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @auther 传奇后
 * @date 2022/11/8 15:18
 * @veersion 1.0
 */
@RestController
@RequestMapping("/news")
@CrossOrigin
public class NewsController {

    @Resource
    private NewsService newsService;

    /**
     * 查询首页position新闻
     * @param position
     * @return
     */
    @GetMapping("/queryPosition")
    public ResultVo queryBigNews(Integer position) {
        return newsService.queryBigNews(position);
    }

    /**
     * 查询首页普通news
     * @param categorizeId
     * @return
     */
    @GetMapping("/queryIndexNews")
    public ResultVo queryIndexNews(Long categorizeId) {
        return newsService.queryIndexNews(categorizeId);
    }

    /**
     * 根据新闻Id查询新闻信息
     * @param newsId
     * @return
     */
    @GetMapping("/queryNewsByNewsId")
    public ResultVo selectNewsByNewsId(Long newsId) {
        return newsService.selectNewsByNewsId(newsId);
    }

    @GetMapping("/queryNewsByCategorize")
    public ResultVo queryNewsByCategorize(Long categoryId,Integer pageNum,Integer pageSize){
        Integer skipCount = (pageNum-1)*pageSize;
        return newsService.queryNewsByCategorize(categoryId,skipCount,pageSize);
    }

    /**
     * 分页查询新闻
     * @param categoryId
     * @param title
     * @param pageNum
     * @param pageSize
     * @return
     */
        @GetMapping("/page")
        public ResultVo page(Long categoryId,String title,Integer pageNum,Integer pageSize){
            Integer skipCount = (pageNum-1)*pageSize;
            return newsService.page(categoryId,title,skipCount,pageSize);
        }

    @GetMapping("/queryNewsByRecent")
    public ResultVo queryNewsByRecent(){
        return newsService.queryNewsByRecent();
    }

    /**
     * 删除信息
     * @param news
     * @param token
     * @return
     */
    @PostMapping("/delete")
    public ResultVo deleteNews(@RequestBody News news, @RequestHeader String token) {
        return newsService.deleteById(news);
    }

    @PostMapping("/add")
    public ResultVo addNews(@RequestBody NewsVo newsVo, @RequestHeader String token){
        System.out.println(newsVo);
        return newsService.add(newsVo);
    }

    @PostMapping("/update")
    public ResultVo updateNews(@RequestBody NewsVo newsVo, @RequestHeader String token){
        System.out.println(newsVo);
        return newsService.update(newsVo);
        //return new ResultVo(1001, "success", newsVo);
    }

}
