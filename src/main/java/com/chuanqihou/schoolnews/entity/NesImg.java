package com.chuanqihou.schoolnews.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther 传奇后
 * @date 2022/11/9 16:50
 * @veersion 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NesImg implements Serializable {
    private Long id;
    private Long newsId;
    private String url;
    private Date createTime;
    private Date updateTime;
}
