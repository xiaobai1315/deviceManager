package com.study.logcat.controller;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author zhanghongjie11
 * @date 2021/7/8 7:12 下午
 * @description
 */
public class Test {

    private ChannelSftp channelSftp;

    private Session session = null;

    private int timeout = 60000;

    public void connect() throws JSchException {

//        System.out.println("try connect to  " + conf.getHost() + ",username: " + conf.getUsername() + ",password: " + conf.getPassword() + ",port: " + conf.getPort());

        JSch jSch = new JSch();
        session = jSch.getSession("admin", "11.91.200.124",22);
        session.setPassword("AdminTest2!_+");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(30000);

    }

    /**
     * 遍历文件名
     * @param directory
     * @return
     * @throws Exception
     */
    public List<String> listFiles(String directory) throws Exception {
        //通道
        channelSftp = (ChannelSftp) session.openChannel("sftp");
        //连接通道
        channelSftp.connect();
        Vector fileList;
        List<String> fileNameList = new ArrayList<String>();
        fileList = channelSftp.ls(directory);
        Iterator it = fileList.iterator();

        while (it.hasNext()) {

            String fileName = ((ChannelSftp.LsEntry) it.next()).getFilename();

            if (".".equals(fileName) || "..".equals(fileName)) {

                continue;
            }

            fileNameList.add(fileName);
            session.disconnect();

            channelSftp.quit();
        }

        return fileNameList;

    }

    public void downLoadFile(String fileName) throws SftpException {

        SftpATTRS attr = channelSftp.stat(fileName);
        long fileSize = attr.getSize();
        String dst = "D:\\INTPahcfg.tar.gz";
        channelSftp.get(fileName, dst);

    }

    public void close() {

        session.disconnect();

    }

    /**
     * 查看文件
     * @param src
     * @param filename
     * @return
     * @throws JSchException
     * @throws SftpException
     * @throws InterruptedException
     * @throws IOException
     */

    public String catFile(String src, String filename) throws JSchException, SftpException, InterruptedException, IOException {

        //通道
        channelSftp = (ChannelSftp) session.openChannel("sftp");
        //连接通道
        channelSftp.connect();
        StringBuffer sb = new StringBuffer();
        try {
            //String fileName = src.substring(src.lastIndexOf("/") + 1);

            //无论是文件夹还是文件打包下载
            //打开通道，设置通道类型，和执行的命令
            Channel channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec) channel;

            // String srcPath = src.substring(0, src.lastIndexOf("/"));

            //   filename = fileName + "_" + UUID.randomUUID().toString();


            //将"/" 的文件夹打包成名为/run/user/download/filename.zip
            String cmdGet = "cat " + filename;
            channelExec.setCommand("cd " + src + ";" + cmdGet);

            channelExec.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader
                    (channelExec.getInputStream()));

            channelExec.connect();
            String line = "";


            while ((line = input.readLine()) != null) {

                sb.append(line + "\n");
                System.out.println(line);// 打印控制台输出
                System.out.println("-------------------------------------分类---------------------");

            }
            System.out.println(sb);

            // 关闭通道
            channelExec.disconnect();

            input.close();

            //关闭session
            session.disconnect();

            channelSftp.quit();

        } catch (Exception e) {
            throw e;
        }
        return sb.toString();

    }

    public static void main(String[] args) {


        try{
            Test test = new Test();
//            test.close();
            test.connect();
            String directory = "/export/Logs/server1/";
              List<String> listFilename = test.listFiles(directory);

                System.out.println(listFilename);

//            String filename = "test1.sh";
//            sshUtil.catFile(directory,filename);

            test.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
