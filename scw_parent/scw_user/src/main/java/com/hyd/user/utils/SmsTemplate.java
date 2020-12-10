package com.hyd.user.utils;

import com.hyd.common.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsTemplate {

    @Value("${sms.host}")
    private String host;
    @Value("${sms.path}")
    private String path;
    @Value("${sms.method}")
    private String method;
    @Value("${sms.appCode}")
    private String appCode;

    public String sendCode(String phoneNum,String code){
        // 请求头
        Map<String,String> header = new HashMap<>();
        header.put("Authorization",appCode);
        header.put("Content-Type","application/x-www-form-urlencoded");

        // 请求参数
        Map<String,String> query = new HashMap<>();
        query.put("mobile",phoneNum);
        query.put("param","code:"+code);
        query.put("tpl_id","TP1711063");

        try {
            HttpResponse httpResponse = HttpUtils.doPost(host, path, method, header, query, "");
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}
