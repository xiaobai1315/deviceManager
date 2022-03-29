package com.study.Framework.utils;

import com.alibaba.fastjson.JSONObject;
import com.study.Framework.webResult.WebResultConsts;
import org.testng.Assert;

/**
 * @author zhanghongjie11
 * @date 2021/5/31 5:30 下午
 * @description
 */
public class AssertUtil {

    /**
     * 网络请求结果是否为200的断言
     * @param jsonObject 网络请求结果
     */
    public static void assertSuccess(JSONObject jsonObject) {
        Assert.assertEquals(jsonObject.get("code"), WebResultConsts.CODE_OK);
    }
}
