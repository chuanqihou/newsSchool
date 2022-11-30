package com.chuanqihou.schoolnews.service;


import com.chuanqihou.schoolnews.entity.User;
import com.chuanqihou.schoolnews.vo.ResultVo;

import java.util.List;

/**
 * @auther 传奇后
 * @date 2022/11/8 15:17
 * @veersion 1.0
 */
public interface UserService {

    ResultVo userRegister(User user);

    ResultVo login(User user);

    ResultVo queryInfo(Long id);

    ResultVo sentMsg(User user);

    ResultVo updateUserById(User user);

    ResultVo check(Long id, String password);

    ResultVo add(User user);

    ResultVo updateUser(User user);

    ResultVo deleteUser(User user);

    ResultVo pageUsers(Integer skipCount, Integer pageSize,String nickName,String phone);

    List<User> queryAllUsers();

    int insertUserByList(List<User> userList);

    ResultVo updateUserAdminById(User user);

    ResultVo updateStatus(User user);
}
