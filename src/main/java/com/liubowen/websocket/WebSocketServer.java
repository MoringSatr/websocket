package com.liubowen.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author liubowen
 * @date 2018/3/14 21:13
 * @description
 */
@Slf4j
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer {

    private SessionContext sessionContext = SessionContext.instance();

    @OnOpen
    public void onOpen(Session session) {
        this.sessionContext.add(session);
        this.sessionContext.sendMessage(session, "连接成功");
        log.info("有新连接加入！ sessionId : {} ,当前在线人数为 : {}", session.getId(), this.sessionContext.currentCount());
    }

    @OnClose
    public void onClose(Session session) {
        this.sessionContext.remove(session);
        log.info("有一连接关闭！ sessionId : {} ,当前在线人数为 : {}", session.getId(), this.sessionContext.currentCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        this.sessionContext.sendMessageToAll(message);
        log.info("来自客户端的消息！ sessionId : {} ,当前在线人数为 : {} , 消息: {}", session.getId(), this.sessionContext.currentCount(), message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        log.error("发生错误");
    }

}
