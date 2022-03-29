package com.study.logcat.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author zhanghongjie11
 * @date 2021/6/17 6:37 下午
 * @description
 */
@Slf4j
public class TerminalService {

    private static PrintStream commander;
    private static Session session;
    private static ChannelSftp chSftp;
    private static Channel channel;

    /**
     * 连接远程服务器的终端
     */
    public static void connectTerminal() {
        try {
            // 连接ssh
            JSch jSch = new JSch();
            session = jSch.getSession("admin", "11.91.200.124",22);
            session.setPassword("AdminTest2!_+");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(30000);
            log.info("session is connected");

            //开启shell通道
            Channel channel = session.openChannel("shell");
            OutputStream inputstream_for_the_channel = channel.getOutputStream();
            commander = new PrintStream(inputstream_for_the_channel, true);
            channel.connect();

            // channel.getInputStream 从终端读取返回的数据
            InputStream outputstream_from_the_channel = channel.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(outputstream_from_the_channel));
            String line;
            while ((line = br.readLine()) != null) {
//                System.out.print(line+"\n");
                log.info(line);
                WebSocketServerService.sendMessageToWeb(line);
            }

            do {
                Thread.sleep(1000);
            } while(!channel.isEOF());

        }catch (Exception e) {
            if (session != null) {
                session.disconnect();
            }
            e.printStackTrace();
        }
    }

    /**
     * 断开终端
     */
    public static void disConnectTerminal() {
        session.disconnect();
    }

    /**
     * 终端发送指令
     * @param command 指令
     */
    public static void sendCommand(String command) {
        log.info("send command to terminal => " + command);
        commander.println(command);
    }


    public static void main(String[] args) {

    }
}
