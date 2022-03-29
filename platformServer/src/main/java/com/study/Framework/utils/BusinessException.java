package com.study.Framework.utils;

/**
 * @author zhanghongjie11
 * @date 2021/5/31 5:28 下午
 * @description
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
        LogUtil.info(message);
    }
}