package com.chuanqihou.schoolnews.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @auther 传奇后
 * @date 2022/11/8 15:11
 * @veersion 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultVo {
    private Integer code;
    private String message;
    private Object data;
}
