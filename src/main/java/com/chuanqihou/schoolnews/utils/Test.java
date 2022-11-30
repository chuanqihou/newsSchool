package com.chuanqihou.schoolnews.utils;


/**
 * @auther 传奇后
 * @date 2022/11/12 0:21
 * @veersion 1.0
 */
public class Test {
    public static void main(String[] args) {
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        System.out.println(code);
        SMSUtils.sendMessage("传奇后","SMS_241351714","18774137163",code);
    }
}
