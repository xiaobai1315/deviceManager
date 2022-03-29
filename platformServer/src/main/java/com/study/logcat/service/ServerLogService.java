package com.study.logcat.service;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author zhanghongjie11
 * @date 2021/9/22 2:45 下午
 * @description
 */
@Slf4j
public class ServerLogService {

    private static Session session;
    private static ChannelSftp chSftp;
    private static Channel channel;

    public void connectToServer() {
        try {
            // 连接ssh
            JSch jSch = new JSch();
            session = jSch.getSession("admin", "11.91.200.124",22);
            session.setPassword("AdminTest2!_+");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(30000);
            log.info("session is connected");

            log.info("Opening Channel.");
            Channel channel = session.openChannel("sftp"); // 打开SFTP通道
            channel.connect(); // 建立SFTP通道的连接
            chSftp = (ChannelSftp) channel;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chSftp.quit();
            if (channel != null) {
                channel.disconnect();
                log.info("channel disconnect");
            }
            if (session != null) {
                session.disconnect();
                log.info("channel disconnect");
            }
        }
    }

    /**
     * 获取远程服务器日志文件列表
     * @param downloadFile 下载文件的路径
     */
    public List<String> getLogDir(String downloadFile) {
        List<String> fileList = new ArrayList<>();
        try {
            Vector ls = chSftp.ls(downloadFile);
            Iterator iterator = ls.iterator();
            while (iterator.hasNext()) {
                String filename = ((ChannelSftp.LsEntry) iterator.next()).getFilename();
                if (filename.endsWith(".log")) {
                    System.out.println(filename);
                    fileList.add(filename);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileList;
    }

    /**
     * 下载远程服务器日志文件
     * @param downloadFile 下载文件的路径
     */
    public void download(String downloadFile) {
        try {

            String directory = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("static").getPath();
            String[] split = downloadFile.split("/");
            String fileName = split[split.length - 1];

            // 连接ssh
            JSch jSch = new JSch();
            session = jSch.getSession("admin", "11.91.200.124",22);
            session.setPassword("AdminTest2!_+");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(30000);
            log.info("session is connected");

            log.info("Opening Channel.");
            Channel channel = session.openChannel("sftp"); // 打开SFTP通道
            channel.connect(); // 建立SFTP通道的连接
            chSftp = (ChannelSftp) channel;
            SftpATTRS attr = chSftp.stat(downloadFile);
            long fileSize = attr.getSize();
            chSftp.get(downloadFile, directory, null); // 代码段1

            System.out.println("http://localhost:8081/" + fileName);
            log.info("成功下载文件至"+directory);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chSftp.quit();
            if (channel != null) {
                channel.disconnect();
                log.info("channel disconnect");
            }
            if (session != null) {
                session.disconnect();
                log.info("channel disconnect");
            }
        }
    }

    /**
     * 读本地文件
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static void main(String[] args) {

        new ServerLogService().getLogDir( "/export/Logs/server1/");
    }
}


