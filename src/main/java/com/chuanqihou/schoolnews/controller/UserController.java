package com.chuanqihou.schoolnews.controller;

import com.chuanqihou.schoolnews.entity.User;
import com.chuanqihou.schoolnews.service.UserService;
import com.chuanqihou.schoolnews.utils.HSSFUtils;
import com.chuanqihou.schoolnews.utils.MD5Utils;
import com.chuanqihou.schoolnews.utils.SnowflakeManager;
import com.chuanqihou.schoolnews.vo.ResultVo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @auther 传奇后
 * @date 2022/11/8 15:18
 * @veersion 1.0
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private SnowflakeManager snowflakeManager;

    @PostMapping("/register")
    public ResultVo register(@RequestBody User user) {
        return userService.userRegister(user);
    }

    @PostMapping("/login")
    public ResultVo login(@RequestBody User user) {
        ResultVo login = userService.login(user);
        return login;
    }

    @GetMapping("/queryInfo")
    public ResultVo queryInfo(Long id) {
        return userService.queryInfo(id);
    }

    @PostMapping("/sendMsg")
    public ResultVo sendMsg(@RequestBody User user) {
        return userService.sentMsg(user);
    }

    @PostMapping("/update")
    public ResultVo updateUserById(@RequestBody User user) {
        return userService.updateUserById(user);
    }

    @PostMapping("/adminUpdate")
    public ResultVo updateUserAdminById(@RequestBody User user,@RequestHeader("token") String token) {
        return userService.updateUserAdminById(user);
    }

    @PostMapping("/check")
    public ResultVo updateUserById(Long id,String password) {
        System.out.println(id);
        return userService.check(id,password);
    }

    /**
     * 后台添加用户
     * @param
     * @param token
     * @return
     */
    @PostMapping("/add")
    public ResultVo add(@RequestBody User userAdd,@RequestHeader("token") String token) {
        return userService.add(userAdd);
    }

    /**
     * 后台更新用户信息
     * @param user
     * @param token
     * @return
     */
    @PostMapping("/updateAdmin")
    public ResultVo update(@RequestBody User user,@RequestHeader("token") String token) {
        return userService.updateUser(user);
    }

    /**
     * 根据用户Id删除（停用）
     * @param user
     * @param token
     * @return
     */
    @PostMapping("/delete")
    public ResultVo delete(@RequestBody User user,@RequestHeader("token") String token) {
        return userService.deleteUser(user);
    }

    @GetMapping("/page")
    public ResultVo pageUsers(Integer pageNum,Integer pageSize,String nickName,String phone){
        Integer skipCount = (pageNum-1)*pageSize;

        return userService.pageUsers(skipCount, pageSize,nickName,phone);
    }

    /**
     * 模板文件下载
     * @param response
     * @throws Exception
     */
    @GetMapping("/modalDownload")
    public void modalDownload(HttpServletResponse response) throws Exception {
        //创建一个excel
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建一个sheet
        HSSFSheet sheet = wb.createSheet("批量添加用户信息");
        //第一行
        HSSFRow row = sheet.createRow(0);
        //第一行第1-10列
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("头像");

        cell = row.createCell(1);
        cell.setCellValue("昵称");

        cell = row.createCell(2);
        cell.setCellValue("手机号");

        cell = row.createCell(3);
        cell.setCellValue("密码");

        cell = row.createCell(4);
        cell.setCellValue("是否管理员（是：1 否：0）");

        cell = row.createCell(5);
        cell.setCellValue("账号状态（封禁：1 正常：0）");


        //设置响应格式
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置响应头
        response.addHeader("Content-Disposition","attachment;filename=UserModal.xls");
        //获取输出流
        OutputStream out = response.getOutputStream();
        //将wb中的数据写到out输出流传至浏览器
        wb.write(out);
        //关闭连接
        wb.close();
        out.close();
    }

    /**
     * 用户信息导出
     * @param response
     * @throws Exception
     */
    @RequestMapping("/fileDownload")
    public void fileDownload(HttpServletResponse response) throws Exception {
        //查询所有信息
        List<User> users = userService.queryAllUsers();
        HSSFWorkbook wb = download(users);
        //设置响应格式
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置响应头
        response.addHeader("Content-Disposition","attachment;filename=Users.xls");
        //获取输出流
        OutputStream out = response.getOutputStream();
        //将wb中的数据写到out输出流传至浏览器
        wb.write(out);
        //关闭连接
        wb.close();
        out.close();
    }

    public HSSFWorkbook download(List<User> users){
        //创建一个excel
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建一个sheet
        HSSFSheet sheet = wb.createSheet("用户信息");
        //第一行
        HSSFRow row = sheet.createRow(0);
        //第一行第1-10列

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户id");

        cell = row.createCell(1);
        cell.setCellValue("头像");

        cell = row.createCell(2);
        cell.setCellValue("昵称");

        cell = row.createCell(3);
        cell.setCellValue("身份");

        cell = row.createCell(4);
        cell.setCellValue("账号状态");

        cell = row.createCell(5);
        cell.setCellValue("创建时间");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //判断集合是否有数据
        if (users != null && users.size() > 0) {
            User u = null;
            //循环
            for (int i = 0; i < users.size(); i++) {
                //获取对象
                u = users.get(i);
                //第 i+1 行 第 1-10 列
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(u.getId().toString());
                cell = row.createCell(1);
                cell.setCellValue(u.getHeadImg());
                cell = row.createCell(2);
                cell.setCellValue(u.getNickName());
                cell = row.createCell(3);
                cell.setCellValue(u.getAdmin()==1?"管理员":"普通用户");
                cell = row.createCell(4);
                cell.setCellValue(u.getStatus()==1?"正常":"封禁");
                cell = row.createCell(5);
                cell.setCellValue(sdf.format(u.getCreateTime()));
            }
        }
        return wb;
    }

    /**
     * 批量导入用户信息
     * @return
     */
    @PostMapping("/importUser")
    @ResponseBody
    public Map<Object,Object> importActivity(MultipartFile userFile) {
        //定义返回值map
        Map<Object,Object> map = new HashMap<>();
        try {

/*            //获取上传文件名
            String originalFilename = activityFile.getOriginalFilename();
            //文件存储目录
            File file = new File("E:\\qw",originalFilename);
            //将上传文件存储
            activityFile.transferTo(file);
            //获取文件存储路径
            InputStream in = new FileInputStream("E:\\qw\\"+originalFilename);*/

            //获取上传文件（输入流）
            InputStream in = userFile.getInputStream();
            //使用excel操作类HSSFWorkbook操作文件
            HSSFWorkbook wb = new HSSFWorkbook(in);
            //获取excel中第一个sheet
            HSSFSheet sheet = wb.getSheetAt(0);
            //定义行
            HSSFRow row = null;
            //定义列
            HSSFCell cell = null;
            //定义list<User>集合,用于封装从文件中取出的所有Activity对象
            List<User> userList = new ArrayList<>();
            //定义Activity类，用于接收文件中每一行Activity对象
            User user = null;
            //循环excel中第一个sheet的每一行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                user = new User();
                //获取第 i 行
                row = sheet.getRow(i);
                //设置用户d
                user.setId(Long.valueOf(String.valueOf(snowflakeManager.nextValue()).substring(0, 16)));
                //设置用户创建时间
                user.setCreateTime(new Date());
                //循环第 i 行所有列
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    //定位到第 i 行 第 j 列
                    cell = row.getCell(j);
                    //获取第 i 行 第 j 列的数据
                    String value = HSSFUtils.getCellValue(cell);
                    //按照既定规则将数据添加到Activity对象中
                    if (j == 0) {
                        user.setHeadImg(value);
                    } else if (j == 1) {
                        user.setNickName(value);
                    } else if (j == 2) {
                        user.setPhone(value);
                    } else if (j == 3) {
                        user.setPassword(MD5Utils.md5(value));
                    } else if (j == 4) {
                        user.setAdmin(value.equals("0.0") ? 0L:1L );
                    }else if (j == 5) {
                        user.setStatus(value.equals("0.0") ? 0L:1L );
                    }
                }
                //将第 i 行 转换成Activity对象后封装在List<Activity>集合中
                userList.add(user);
            }
            //将获取到的Activity对象集合保存在数据库中，返回插入数量
            int count = userService.insertUserByList(userList);
            //返回成功插入数量
            map.put("count",count);
            //返回上传状态信息
            map.put("success",true);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            //返回上传状态信息
            map.put("success",false);
            return map;
        }
    }

    @PostMapping("/updateStatus")
    public ResultVo updateStatus(@RequestBody User user,@RequestHeader("token") String token){
        return userService.updateStatus(user);
    }

}
