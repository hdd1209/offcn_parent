package com.hyd.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OSSTemplate {

    private String endpoint; // = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId; //"=LTAI4GFH1VoAD4yEqwh6XFPK";
    private String accessKeySecret; //= "Lz7hiKSeRHbnFjciX1t1MOu5dJUPph";
    private String bucketName; //= "2020-12-08-hyd";
    private String bucketDomain; //="https://2020-12-08-hyd.oss-cn-beijing.aliyuncs.com";

    public String upload(InputStream inputStream,String fileName){
        // 项目多，管理方便，每天一个目录，存放当天文件
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dirName = dateFormat.format(new Date());
        // 避免文件名重复，需要在文件名前加前缀 UUID
        fileName = UUID.randomUUID().toString().replace("-", "")+"_"+fileName;
        // oos的客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //上传
        ossClient.putObject(bucketName,"pic\\"+dirName+"\\"+fileName,inputStream);
        // 上传之后的路径
        String url = bucketDomain+"\\pic\\"+dirName+"\\"+fileName;
        ossClient.shutdown();
        System.out.println("文件存储的路径:"+url);
        return url;
    }

}
