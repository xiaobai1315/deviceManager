package com.study.Framework.utils;

import com.study.Framework.pojo.TestCaseInfo;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;


public class FileUtil {

    /**
     * config.properties 文件地址
     */
    public static String resourcePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    /**
     * 获取config.property 中的配置信息
     * @param fileName 要读取的配置文件名称
     * @param property 要获取的属性名称
     * @return
     */
    public static String getProperty(String fileName, String property) {
        String propertyValue = null;
        try {
            Properties properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            properties.load(bufferedReader);
            propertyValue = properties.getProperty(property);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return propertyValue;
    }

    /**
     * 获取指定包下的类数组
     * @param packageName 包名
     * @return  指定包下的类数组
     */
    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        System.out.println(path);
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }


    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    /**
     * 从测试类中获取有@Test注解的方法
     * @return @Test注解的方法list
     */
    public static ArrayList<TestCaseInfo> getTestMethodList(String testCasePackage) {
        ArrayList<TestCaseInfo> methodList = new ArrayList<>();

        try {
            Class[] classes = getClasses(testCasePackage);
            for (Class aClass : classes) {
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Test.class)) {
                        Test annotation = method.getAnnotation(Test.class);
                        TestCaseInfo testCaseInfo = new TestCaseInfo(annotation.testName(), annotation.groups()[0], annotation.description());
                        methodList.add(testCaseInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodList;
    }
}
