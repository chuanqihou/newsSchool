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
public class Categorize {

  private Long id;
  private String name;
  private Integer ascription;
  private Date createTime;
  private Date updateTime;
  private Long createUserId;
  private Long updateUserId;

}
