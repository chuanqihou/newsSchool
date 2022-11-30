package com.chuanqihou.schoolnews.dao;

import com.chuanqihou.schoolnews.entity.NesImg;

import java.util.List;

/**
 * @auther 传奇后
 * @date 2022/11/9 16:34
 * @veersion 1.0
 */
public interface NewsImgDao {
    List<String> selectImgByNewsId(Long newsId);

    int add(NesImg nesImg);

    void delete(Long id);
}
