package com.chuanqihou.schoolnews.dao;


import com.chuanqihou.schoolnews.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther 传奇后
 * @date 2022/11/8 15:15
 * @veersion 1.0
 */
public interface UserDao {
    User queryUserByName(String nickName);

    int insertUser(User user);

    User queryUserByPhone(String phone);

    User queryInfo(Long id);

    int update(User user);

    int delete(User user);

    List<User> pageUsers(Map<String, Object> tj);

    Integer pageUsersTotal(@Param("phone")String phone,@Param("nickName")String nickName);

    List<User> queryAllUsers();

    int adminUpdate(User user);

    int updateStatus(User user);

    User login(String phone);
}
