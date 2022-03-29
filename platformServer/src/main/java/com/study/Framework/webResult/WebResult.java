package com.study.Framework.webResult;

import java.io.Serializable;
import java.util.HashMap;


public class WebResult extends HashMap<String, Object> implements Serializable {

    public WebResult success() {
        return this.setResultCode(WebResultConsts.CODE_OK);
    }

    public WebResult fail() {
        return this.setResultCode(WebResultConsts.CODE_FAIL);
    }

    public WebResult setResultCode(int code) {
        this.put("code", code);
        return this;
    }

    public int getResultCode() {
        Integer code = (Integer) this.get("code");
        return code == null ? -1 : code;
    }

    public WebResult setResultMessage(String resultMessage) {
        this.put("message", resultMessage);
        return this;
    }

    public String getResultMessage() {
        return this.containsKey("message") ? this.get("message").toString() : null;
    }

    public WebResult setData(Object data) {
        this.put("data", data);
        return this;
    }

    public Object getData() {
        return this.getOrDefault("data", null);
    }
}
