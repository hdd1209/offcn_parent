package com.hyd.common.test;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpDemo {

    public static void main(String[] args) throws IOException {

//        1.创建httpClient对象
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

        //2. 创建一个请求
        HttpGet httpGet = new HttpGet("http://www.baidu.com");

        // 3.发送请求
        HttpResponse response = defaultHttpClient.execute(httpGet);
        // 4.获取响应数据
        System.out.println(response);
        // 5.获取响应结果
        String content = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(content);
    }
}
