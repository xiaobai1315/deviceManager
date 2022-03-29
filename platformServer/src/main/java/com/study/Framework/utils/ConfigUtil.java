package com.study.Framework.utils;

import org.springframework.context.annotation.Configuration;

/**
 * @author zhanghongjie11
 * @date 2021-05-27 10:35 上午
 * @description 获取config.properties文件中的配置项
 */
@Configuration
public class ConfigUtil {

    private static final String CONFIGFILEPATH = FileUtil.resourcePath + "config.properties";

    /**
     * 获取登录的用户名
     * @return 获取登录的用户名
     */
    public static String getLoginUserName() {
        return FileUtil.getProperty(CONFIGFILEPATH, "erp_login_user_name");
    }

    /**
     * 获取登录的用户密码
     * @return 获取登录的用户密码
     */
    public static String getLoginUserPwd() {
        return FileUtil.getProperty(CONFIGFILEPATH, "erp_login_user_password");
    }

    /**
     * 获取登录UID
     * @return 获取登录UID
     */
    public static String getLoginUuid() {
        return FileUtil.getProperty(CONFIGFILEPATH, "erp_login_user_uuid");
    }

    /**
     * 获取租户ID
     * @return 获取租户ID
     */
    public static String getTenentCode() {
        return FileUtil.getProperty(CONFIGFILEPATH, "tenent_code");
    }

    /**
     * 获取园区编码
     * @return 园区编码
     */
    public static String getParkCode() {
        return FileUtil.getProperty(CONFIGFILEPATH, "park_code");
    }

    /**
     * 获取园区名称
     * @return 园区名称
     */
    public static String getParkName() {
        return FileUtil.getProperty(CONFIGFILEPATH, "park_name");
    }

    /**
     * 获取场地名称
     * @return 场地名称
     */
    public static String getStationName() {
        return FileUtil.getProperty(CONFIGFILEPATH, "station_name");
    }

    /**
     * 获取场地编码
     * @return 场地编码
     */
    public static String getStationCode() {
        return FileUtil.getProperty(CONFIGFILEPATH, "station_code");
    }

    /**
     * 获取测试类的包地址
     * @return 测试类的包地址
     */
    public static String getTestCasePackage() {
        return FileUtil.getProperty(CONFIGFILEPATH, "test_case_package");
    }
}
