package com.study.logcat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhanghongjie11
 * @date 2021/6/17 9:38 上午
 * @description
 */
@Slf4j
@ServerEndpoint("/websocket/test")
@Component
public class WebSocketServerService {


    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    /**
     * 建立会话
     *
     * @param session  会话
     */
    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
        log.info("session on open");
        new Thread(new Runnable() {
            @Override
            public void run() {
                TerminalService.connectTerminal();
            }
        }).start();
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(Session session) {
        // 关闭连接时的操作，这里在关闭连接时会调用此方法，可以做退出提示
        sessions.remove(session.getId());
        TerminalService.disConnectTerminal();
    }

    /**
     * 消息发送（有点相像Controller层的方法参数设计，只是多了session）
     *
     * @param session  会话（当前）
     *  @param msg 消息
     */
    @OnMessage
    public void OnMessage(Session session, String msg) {
        // 发送消息操作，当会话发送消息时会调用这个方法
        log.info("OnMessage --->" + msg);
        broadcast(msg);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        // 案例
        log.error("发生错误,用户：{}，发生错误" + session.getId());
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
    public void broadcast(String message) {
        // webSocketSet是存放会话（Session）的集合
        log.info("broadcast --->" + message);
        sessions.forEach((id, session) -> {
            try {
                // 判断会话是否连接
                if (session.isOpen()){
                    // 发送同步信息到当前会话
//                    List<String> strings = LogcatService.remoteExecute(message);
//                    session.getBasicRemote().sendText(strings.toString());
                    TerminalService.sendCommand(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void sendMessageToWeb(String msg) {
        sessions.forEach((id, session) -> {
            synchronized (session) {
                try {
                    session.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
