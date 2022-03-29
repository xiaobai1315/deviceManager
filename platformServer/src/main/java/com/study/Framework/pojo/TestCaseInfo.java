package com.study.Framework.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhanghongjie11
 * @date 2021/6/2 4:21 下午
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseInfo {
    private String testName;
    private String group;
    private String description;
}