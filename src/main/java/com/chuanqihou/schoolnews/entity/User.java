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
public class User {

  private Long id;
  private String headImg;
  private String nickName;
  private String phone;
  private String password;
  private long admin;
  private long status;
  private Date createTime;
  private String msg;
}
