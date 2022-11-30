package com.chuanqihou.schoolnews.controller;

import com.chuanqihou.schoolnews.utils.TencentCOSUtil;
import com.chuanqihou.schoolnews.vo.ResultVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther 传奇后
 * @date 2022/11/9 11:38
 * @veersion 1.0
 */
@RestController
@RequestMapping("/api/cos")
@CrossOrigin
public class CosController {

    /**
     * 文件上传到腾讯云
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResultVo upload(@RequestParam(value = "file") MultipartFile file){
        if (file == null){
            return new ResultVo(1002, "failure", null);
        }
        String uploadfile = TencentCOSUtil.uploadfile(file);
        Map<String, String> map = new HashMap<>();
        map.put("uploadfile",uploadfile);
        try {
            map.put("fileName", uploadfile.substring(70,91));
        } catch (Exception e) {
            return new ResultVo(1002,"fail",null);
        }
        return new ResultVo(1001,"success",map);
    }

    @PostMapping("/uploadAdmin")
    public Map<String,String> uploadAdmin(@RequestParam(value = "file") MultipartFile file){
        String uploadfile = TencentCOSUtil.uploadfile(file);
        Map<String, String> map = new HashMap<>();
        map.put("location",uploadfile);
        map.put("fileName", uploadfile.substring(70,91));
        return map;
    }

}
