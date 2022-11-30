package com.chuanqihou.schoolnews.service.impl;

import com.chuanqihou.schoolnews.dao.UserDao;
import com.chuanqihou.schoolnews.entity.User;
import com.chuanqihou.schoolnews.service.UserService;
import com.chuanqihou.schoolnews.utils.MD5Utils;
import com.chuanqihou.schoolnews.utils.SMSUtils;
import com.chuanqihou.schoolnews.utils.SnowflakeManager;
import com.chuanqihou.schoolnews.utils.ValidateCodeUtils;
import com.chuanqihou.schoolnews.vo.ResultVo;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @auther 传奇后
 * @date 2022/11/8 15:26
 * @veersion 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private SnowflakeManager snowflakeManager;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 用户注册
     * @param user 用户信息
     * @return
     */
    @Override
    @Transactional
    public ResultVo userRegister(User user) {
        //synchronized 锁
        synchronized (this) {
            try {
                //以手机号为key在redis中查询验证码信息
                String code = (String) redisTemplate.opsForValue().get(user.getPhone());
                String oCode = user.getMsg();
                if (code == null) {
                    return new ResultVo(1002, "手机号验证码不匹配", null);
                }
                if (code != null && !code.equals(oCode)) {
                    return new ResultVo(1002, "验证码错误！！", null);
                }
                User u = userDao.queryUserByName(user.getNickName());
                User u2 = userDao.queryUserByPhone(user.getPhone());
                if (u == null && u2 == null) {
                    //密码md5加密
                    String md5password = MD5Utils.md5(user.getPassword());
                    user.setPassword(md5password);
                    user.setAdmin(0);
                    user.setStatus(0);
                    user.setId(Long.valueOf(String.valueOf(snowflakeManager.nextValue()).substring(0, 16)));
                    user.setCreateTime(new Date());
                    int i = userDao.insertUser(user);
                    if (i > 0) {
                        return new ResultVo(1001, "注册成功！", user);
                    } else {
                        return new ResultVo(1002, "注册失败，未知错误", null);
                    }
                } else {
                    return new ResultVo(1002, "用户名或手机号已经存在！", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 用户登录
     * @param user 用户信息
     * @return
     */
    @Override
    public ResultVo login(User user) {
        User u = userDao.queryUserByPhone(user.getPhone());

        User us = userDao.login(user.getPhone());

        if (us == null) {
            return new ResultVo(1003, "账号已封禁，请联系管理员！",null);
        }

        if (!(user.getMsg() == "" || user.getMsg() == null)) {
            String code = (String) redisTemplate.opsForValue().get(user.getPhone());
            String oCode = user.getMsg();
            if (code == null) {
                return new ResultVo(1002, "未知错误", null);
            }
            if (code != null && !code.equals(oCode)) {
                return new ResultVo(1002, "验证码错误！！", null);
            }
            //生成token
            JwtBuilder jwtBuilder = Jwts.builder();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("key1", "value1");
            //token设置加密
            String token = jwtBuilder.setSubject(u.getNickName())
                    .setIssuedAt(new Date())
                    .setId(u.getId().toString())
                    .setClaims(hashMap)
                    .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                    .signWith(SignatureAlgorithm.HS256, "csmznews")
                    .compact();

            return new ResultVo(1001, token, u);

        }
        String md5pwd = MD5Utils.md5(user.getPassword());
        if (u != null && md5pwd.equals(u.getPassword())) {
            //生成token
            JwtBuilder jwtBuilder = Jwts.builder();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("key1", "value1");
            String token = jwtBuilder.setSubject(u.getNickName())
                    .setIssuedAt(new Date())
                    .setId(u.getId().toString())
                    .setClaims(hashMap)
                    .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                    .signWith(SignatureAlgorithm.HS256, "csmznews")
                    .compact();

            return new ResultVo(1001, token, u);
        }
        return new ResultVo(1002, "用户名或密码错误！", null);
    }

    /**
     * 查询用户详细信息
     * @param id  用户id
     * @return
     */
    @Override
    public ResultVo queryInfo(Long id) {
        User user = userDao.queryInfo(id);
        if (user != null) {
            return new ResultVo(1001, "success", user);
        }
        return new ResultVo(1002, "未知错误", null);
    }

    /**
     * 调用阿里云接口发送短信验证码
     * @param user  用户信息
     * @return
     */
    @Override
    public ResultVo sentMsg(User user) {
        String phone = user.getPhone();
        String cod = (String) redisTemplate.opsForValue().get(phone);
        if (cod != null) {
            return new ResultVo(1003, "fail", null);
        }
        if (StringUtils.hasText(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            System.out.println("验证码：" + code);
            //调用阿里云接口发送短信
            SMSUtils.sendMessage("传奇后","SMS_241351714",phone,code);
            //将手机号和验证码保存到redis之中
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return
     */
    @Override
    public ResultVo updateUserById(User user) {
        if (!(user.getMsg() == "" || user.getMsg() == null)) {
            String code = (String) redisTemplate.opsForValue().get(user.getPhone());
            String oCode = user.getMsg();
            if (code == null) {
                return new ResultVo(1002, "手机号验证码不匹配", null);
            }
            if (code != null && !code.equals(oCode)) {
                return new ResultVo(1002, "验证码错误！！", null);
            }
        }
        String md5password = MD5Utils.md5(user.getPassword());
        user.setPassword(md5password);
        int i = userDao.update(user);
        if (i > 0) {
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }

    /**
     * 检查用户原密码
     * @param id    用户id
     * @param password  用户密码
     * @return
     */
    @Override
    public ResultVo check(Long id, String password) {

        User user = userDao.queryInfo(id);

        String md5password = MD5Utils.md5(password);

        if (user.getPassword().equals(md5password)) {
            return new ResultVo(1001, "success", null);
        }

        return new ResultVo(1002, "原密码错误", null);
    }

    /**
     * 添加用户
     * @param user 用户信息
     * @return
     */
    @Override
    public ResultVo add(User user) {
        try {
            User u = userDao.queryUserByName(user.getNickName());
            User u2 = userDao.queryUserByPhone(user.getPhone());
            if (u == null && u2 == null) {
                String md5password = MD5Utils.md5(user.getPassword());
                user.setPassword(md5password);
                user.setId(Long.valueOf(String.valueOf(snowflakeManager.nextValue()).substring(0, 16)));
                user.setCreateTime(new Date());
                int i = userDao.insertUser(user);
                if (i > 0) {
                    return new ResultVo(1001, "添加成功！", user);
                } else {
                    return new ResultVo(1002, "添加失败，未知错误", null);
                }
            } else {
                return new ResultVo(1002, "用户名或手机号已经存在！", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新用户密码
     * @param user 用户信息
     * @return
     */
    @Override
    public ResultVo updateUser(User user) {
        String md5password = MD5Utils.md5(user.getPassword());
        user.setPassword(md5password);
        int i = userDao.update(user);
        if (i > 0) {
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }

    /**
     * 删除用户
     * @param user 用户信息
     * @return
     */
    @Override
    public ResultVo deleteUser(User user) {
        int i = userDao.delete(user);
        if (i > 0) {
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }

    /**
     * 根据条件查询用户并分页
     * @param skipCount 起始数（从第几条数据开始查）
     * @param pageSize  每页数据条数
     * @param nickName  条件：用户昵称
     * @param phone     条件：手机号
     * @return
     */
    @Override
    public ResultVo pageUsers(Integer skipCount, Integer pageSize, String nickName, String phone) {
        Map<String, Object> tj = new HashMap<>();
        tj.put("skipCount", skipCount);
        tj.put("pageSize", pageSize);
        tj.put("nickName", nickName);
        tj.put("phone", phone);
        List<User> users = userDao.pageUsers(tj);
        Integer total = userDao.pageUsersTotal(phone, nickName);
        Map<String, Object> map = new HashMap<>();
        map.put("users", users);
        map.put("total", total);
        return new ResultVo(1001, "success", map);
    }

    /**
     *查询所有用户信息数据 导出excel
     * @return
     */
    @Override
    public List<User> queryAllUsers() {
        return userDao.queryAllUsers();
    }

    /**
     * 批量导入（插入）用户信息
     * @param userList
     * @return
     */
    @Override
    public int insertUserByList(List<User> userList) {
        int count = 0;
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            userDao.insertUser(user);
            count++;
        }
        return count;
    }

    /**
     * 更新管理员密码
     * @param user 用户信息
     * @return
     */
    @Override
    public ResultVo updateUserAdminById(User user) {
        String md5password = MD5Utils.md5(user.getPassword());
        user.setPassword(md5password);
        int i = userDao.adminUpdate(user);
        if (i > 0) {
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }

    /**
     * 更新用户状态信息
     * @param user 用户信息
     * @return
     */
    @Override
    public ResultVo updateStatus(User user) {
        int j = userDao.updateStatus(user);
        if (j > 0) {
            return new ResultVo(1001, "success", null);
        }
        return new ResultVo(1002, "fail", null);
    }
}
