package com.chuanqihou.schoolnews.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Random;

/**
 * @auther 传奇后
 * @date 2022/11/9 11:37
 * @veersion 1.0
 */
public class TencentCOSUtil {
    // 存储桶名称
    private static final String bucketName = "";
    //secretId 秘钥id
    private static final String SecretId = "";
    //SecretKey 秘钥
    private static final String SecretKey = "";
    // 腾讯云 自定义文件夹名称
    private static final String prefix = "";
    // 访问域名
    public static final String URL = "";
    // 创建COS 凭证
    private static COSCredentials credentials = new BasicCOSCredentials(SecretId,SecretKey);
    // 配置 COS 区域 就购买时选择的区域 我这里是 广州（guangzhou）
    private static ClientConfig clientConfig = new ClientConfig(new Region(""));

    public static String uploadfile(MultipartFile file){
        // 创建 COS 客户端连接
        COSClient cosClient = new COSClient(credentials,clientConfig);
        String fileName = file.getOriginalFilename();
        try {
            String substring = fileName.substring(fileName.lastIndexOf("."));
            File localFile = File.createTempFile(String.valueOf(System.currentTimeMillis()),substring);
            file.transferTo(localFile);
            Random random = new Random();
            fileName =prefix+random.nextInt(10000)+System.currentTimeMillis()+substring;
            // 将 文件上传至 COS
            PutObjectRequest objectRequest = new PutObjectRequest(bucketName,fileName,localFile);
            cosClient.putObject(objectRequest);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cosClient.shutdown();
        }
        return URL+fileName;
    }
}
