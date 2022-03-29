package com.study.Framework.webResult;

public class WebResultUtil {

    /**
     * 请求成功
     * @return
     */
    public static WebResult success() {
        WebResult webResult = new WebResult();
        webResult.success();
        webResult.setResultMessage(WebResultConsts.MESSAGE_OK);
        return webResult;
    }

    /**
     * 请求成功
     * @param object 成功的数据
     * @return
     */
    public static WebResult success(Object object) {
        WebResult webResult = new WebResult();
        webResult.success();
        webResult.setResultMessage(WebResultConsts.MESSAGE_OK);
        webResult.setData(object);
        return webResult;
    }

    /**
     * 请求成功
     * @param object 成功的数据
     * @param message 成功的消息
     * @return
     */
    public static WebResult success(Object object, String message) {
        WebResult webResult = new WebResult();
        webResult.success();
        webResult.setResultMessage(message);
        webResult.setData(object);
        return webResult;
    }

    /**
     * 请求失败
     * @param code 错误码
     * @return
     */
    public static WebResult fail(int code) {
        WebResult webResult = new WebResult();
        webResult.setResultCode(code);
        webResult.setResultMessage(WebResultConsts.MESSAGE_ERROR);
        return webResult;
    }

    /**
     * 请求失败
     * @param message 错误信息
     * @return
     */
    public static WebResult fail(String message) {
        WebResult webResult = new WebResult();
        webResult.fail();
        webResult.setResultMessage(message);
        return webResult;
    }

}
