package com.chuanqihou.schoolnews.controller;


import com.chuanqihou.schoolnews.entity.Comment;
import com.chuanqihou.schoolnews.entity.User;
import com.chuanqihou.schoolnews.service.CommentService;
import com.chuanqihou.schoolnews.vo.CommentVo;
import com.chuanqihou.schoolnews.vo.ResultVo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * @auther 传奇后
 * @date 2022/11/8 15:19
 * @veersion 1.0
 */
@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("/queryByNewsId")
    public ResultVo queryCommentByNewsId(Long newsId) {
        return commentService.queryCommentByNewsId(newsId);
    }

    @PostMapping ("/add")
    public ResultVo addComment(@RequestBody Comment comment, @RequestHeader("token") String token) {
        System.out.println(comment);
        return commentService.insertByNewsIdAndUserId(comment);
    }

    @GetMapping("/page")
    public ResultVo page(Integer status,String commentDetail,String commentUser,Integer pageNum,Integer pageSize){
        Integer skipCount = (pageNum-1)*pageSize;
        return commentService.page(status,commentDetail,commentUser,skipCount,pageSize);
    }

    @PostMapping("/delete")
    public ResultVo delete(@RequestBody Comment comment, @RequestHeader("token") String token) {
        return commentService.deleteComment(comment);
    }

    @PostMapping("/updateStatus")
    public ResultVo updateStatus(@RequestBody Comment comment, @RequestHeader("token") String token) {
        return commentService.updateStatus(comment);
    }

    /**
     * 评论导出
     * @param response
     * @throws Exception
     */
    @RequestMapping("/fileDownload")
    public void fileDownload(HttpServletResponse response) throws Exception {
        //查询所有市场活动信息
        List<CommentVo> commentVos = commentService.queryAllComments();
        HSSFWorkbook wb = download(commentVos);
        //设置响应格式
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置响应头
        response.addHeader("Content-Disposition","attachment;filename=Comments.xls");
        //获取输出流
        OutputStream out = response.getOutputStream();
        //将wb中的数据写到out输出流传至浏览器
        wb.write(out);
        //关闭连接
        wb.close();
        out.close();
    }

    public HSSFWorkbook download(List<CommentVo> commentVos){
        //创建一个excel
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建一个sheet
        HSSFSheet sheet = wb.createSheet("评论信息");
        //第一行
        HSSFRow row = sheet.createRow(0);
        //第一行第1-10列

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("评论id");

        cell = row.createCell(1);
        cell.setCellValue("评论内容");

        cell = row.createCell(2);
        cell.setCellValue("评论用户");

        cell = row.createCell(3);
        cell.setCellValue("评论新闻");

        cell = row.createCell(4);
        cell.setCellValue("状态");

        cell = row.createCell(5);
        cell.setCellValue("创建时间");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //判断市场活动集合是否有数据
        if (commentVos != null && commentVos.size() > 0) {
            CommentVo u = null;
            //循环
            for (int i = 0; i < commentVos.size(); i++) {
                //获取市场活动对象
                u = commentVos.get(i);
                //第 i+1 行 第 1-10 列
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(u.getId().toString());
                cell = row.createCell(1);
                cell.setCellValue(u.getCommentContent());
                cell = row.createCell(2);
                cell.setCellValue(u.getCreateUserNickName());
                cell = row.createCell(3);
                cell.setCellValue(u.getNewsTitle());
                cell = row.createCell(4);
                cell.setCellValue(u.getIsDelete()==0?"正常":"不予展示");
                cell = row.createCell(5);
                cell.setCellValue(u.getCreateTime());
            }
        }
        return wb;
    }

}
