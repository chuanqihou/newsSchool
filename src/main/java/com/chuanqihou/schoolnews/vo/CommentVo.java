package com.chuanqihou.schoolnews.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentVo implements Serializable {

  private Long id;
  private Long newsId;
  private Long createUserId;
  private String commentContent;
  private String createTime;
  private Integer isDelete;

  private String createUserNickName;

  private String newsTitle;

  private String userHeadImg;

}
