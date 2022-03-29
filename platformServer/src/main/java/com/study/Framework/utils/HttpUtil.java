package com.study.Framework.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

    /**
     * post 请求
     * @param url 请求地址
     * @param params 请求体
     * @return 请求结果
     */
    public static JSONObject post(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeaders(Const.requestHeader());
        try {
            httpPost.setEntity(new StringEntity(params, "UTF-8"));
            CloseableHttpResponse res = HttpClients.custom().build().execute(httpPost);
            return JSONObject.parseObject(EntityUtils.toString(res.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get 请求
     * @param url 请求地址
     * @return 请求结果
     */
    public static JSONObject get(String url) {
        HttpGet httpGet = new HttpGet(Const.SWITCH);
        httpGet.setHeaders(Const.requestHeader());

        try {
            CloseableHttpResponse res = HttpClients.custom().build().execute(httpGet);
            return JSONObject.parseObject(EntityUtils.toString(res.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
