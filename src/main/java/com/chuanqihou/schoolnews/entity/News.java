package com.chuanqihou.schoolnews.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class News {

  private Long id;
  private Long categorizeId;
  private String newsTitle;
  private String newsContent;
  private String source;
  private String video;
  private Integer position;
  private Date createTime;
  private Date updateTime;
  private Long createUserId;
  private Long updateUserId;

}
