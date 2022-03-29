package com.study.logcat;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author zhanghongjie11
 * @date 2021/9/14 6:00 下午
 * @description
 */

@RestController
@RequestMapping("/testssh2")
public class TestSSH2 {

    private Channel channel;
    private OutputStream outputStream;
    private PrintStream commander;

    public Channel getChannel() {
        return channel;
    }

    @RequestMapping("/connect")
    private void connectToSsh() throws JSchException, IOException {
        Properties config = new Properties();
        // SSH 连接远程主机时，会检查主机的公钥。如果是第一次该主机，会显示该主机的公钥摘要，提示用户是否信任该主机
        config.put("StrictHostKeyChecking", "no");
        JSch jSch = new JSch();
        Session session = jSch.getSession("admin", "11.91.200.124",22);
        session.setPassword("AdminTest2!_+");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(30000);
        System.out.println("session is connected");

        //开启shell通道
        channel = session.openChannel("shell");
        InputStream inputStream = channel.getInputStream();
//        outputStream = channel.getOutputStream();

        OutputStream inputstream_for_the_channel = channel.getOutputStream();
        commander = new PrintStream(inputstream_for_the_channel, true);

        //通道连接超时时间3s
        channel.connect(3000);

        //读取终端返回的信息流
        try  {
            //循环读取
            byte[] buffer = new byte[2048];
            int i;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                byte[] bytes = Arrays.copyOfRange(buffer, 0, i);
                String s = new String(bytes);
                System.out.println("receive msg => " + s);
            }
        } catch (IOException e) {
            System.out.println("读取终端返回的信息流异常：" +  e);
        } finally {
            //断开连接后关闭会话
            System.out.println("session is disconnected");
            session.disconnect();
            channel.disconnect();
        }
    }

    private void sendToTerminal(Channel channel, String command) {
        if (channel != null) {
            try {
                System.out.println("send msg => " + command);
//                OutputStream outputStream = channel.getOutputStream();
//                outputStream.write(command.getBytes());
//                outputStream.flush();
                commander.println(command);
            } catch (Exception e) {
                System.out.println("web shell将消息转发到终端异常:{}" + e.getMessage());
            }
        }
    }

    @RequestMapping("/sendmsg")
    public void sendmsg() {
//        sendToTerminal(channel, "ls");
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("pwd");
//        strings.add("ls");
//        strings.add("ls -l");
//
//        int v = (int) (Math.random() * 10 % 3);
//        String s = strings.get(v);
        commander.println("tail -f -n100 /export/Logs/info.log");
    }

//    @RequestMapping("/ssh2")
//    private void connectToSsh1() throws JSchException, IOException {
//        Properties config = new Properties();
//        // SSH 连接远程主机时，会检查主机的公钥。如果是第一次该主机，会显示该主机的公钥摘要，提示用户是否信任该主机
//        config.put("StrictHostKeyChecking", "no");
//        JSch jSch = new JSch();
//        Session session = jSch.getSession("admin", "11.91.200.124",22);
//        session.setPassword("AdminTest2!_+");
//        session.setConfig("StrictHostKeyChecking", "no");
//        session.connect(30000);
//        System.out.println("session is connected");
//
//        //开启shell通道
//        channel = session.openChannel("shell");
//        channel.setInputStream(System.in);
//        channel.setOutputStream(System.out);
//        channel.connect(3*1000);
//    }

    @RequestMapping("/ssh3")
    private void connectToSsh3() throws JSchException, IOException, InterruptedException {
        Properties config = new Properties();
        // SSH 连接远程主机时，会检查主机的公钥。如果是第一次该主机，会显示该主机的公钥摘要，提示用户是否信任该主机
        config.put("StrictHostKeyChecking", "no");
        JSch jSch = new JSch();
        Session session = jSch.getSession("admin", "11.91.200.124",22);
        session.setPassword("AdminTest2!_+");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(30000);
        System.out.println("session is connected");

        //开启shell通道
        Channel channel = session.openChannel("shell");
        OutputStream inputstream_for_the_channel = channel.getOutputStream();
        commander = new PrintStream(inputstream_for_the_channel, true);

//        channel.setOutputStream(System.out, true);

        channel.connect();

        InputStream outputstream_from_the_channel = channel.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(outputstream_from_the_channel));
        String line;
        while ((line = br.readLine()) != null)
            System.out.print(line+"\n");

        do {
            Thread.sleep(1000);
        } while(!channel.isEOF());

//        session.disconnect();
    }



    public static void main(String[] args) throws JSchException, InterruptedException, IOException {
//        TestSSH2 testSSH2 = new TestSSH2();
//        new Thread(() -> {
//            try {
//                testSSH2.connectToSsh1();
//                System.out.println("thread is started.....");
//            } catch (JSchException | IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        Thread.sleep(5000);
//        testSSH2.sendToTerminal(testSSH2.getChannel(),"pwd");

    }
}
