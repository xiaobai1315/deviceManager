package com.study.logcat;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanghongjie11
 * @date 2021/9/14 5:30 下午
 * @description
 */
public class TestSSh {

    private static ChannelExec channel = null;

    public static Session getSession() throws JSchException {
        JSch jSch = new JSch();
        Session session = jSch.getSession("admin", "11.91.200.124",22);
        session.setPassword("AdminTest2!_+");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(30000);

        return session;
    }

    public static List<String> remoteExecute(Session session, String command) throws JSchException {
        System.out.println(">> " + command);
        List<String> resultLines = new ArrayList<>();
//        ChannelExec channel = null;
        try{
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream input = channel.getInputStream();
            channel.connect(10000);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String inputLine = null;
                while((inputLine = inputReader.readLine()) != null) {
                    System.out.println("   " + inputLine);
                    resultLines.add(inputLine);
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                        System.out.println("JSch inputStream close error:" + e);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOcxecption:" + e);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                    System.out.println("JSch channel disconnect error:"+ e);
                }
            }
        }
        return resultLines;
    }

    public static void main(String[] args) throws Exception {

        Session session = getSession();
        if (session.isConnected()) {
            System.out.println("Host({}) connected.");
        }
        remoteExecute(session, "pwd");
        remoteExecute(session, "touch test");
        remoteExecute(session, "cd /home");
        remoteExecute(session, "pwd");
        session.disconnect();
    }
}
