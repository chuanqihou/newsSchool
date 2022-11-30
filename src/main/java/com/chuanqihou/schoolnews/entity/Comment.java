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
public class Comment {

  private Long id;
  private Long newsId;
  private Long createUserId;
  private String commentContent;
  private Date createTime;
  private Integer isDelete;

}
