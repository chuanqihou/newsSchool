package com.chuanqihou.schoolnews.vo;

import com.chuanqihou.schoolnews.entity.NesImg;
import com.chuanqihou.schoolnews.entity.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewsVo implements Serializable {

  private Long id;
  private Long categorizeId;
  private String newsTitle;
  private String newsContent;
  private String source;
  private Integer position;
  private long isSmall;
  private String createTime;
  private String updateTime;
  private Long createUserId;
  private Long updateUserId;
  private String video;

  private String years;
  private String day;

  private List<NesImg> images;

  private String categoryName;
  private String createUserName;

  private String[] imgs;
}
