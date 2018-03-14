package com.liubowen.websocket;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;

/**
 * @author liubowen
 * @date 2018/3/14 20:34
 * @description
 */
public class SessionContext {

    private static SessionContext instance;

    private Map<String, Session> sessionMap;

    private SessionContext() {
        this.sessionMap = Maps.newConcurrentMap();
    }

    public static SessionContext instance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    public void add(Session session) {
        this.sessionMap.put(session.getId(), session);
    }

    public Session get(String sessionId) {
        return this.sessionMap.get(sessionId);
    }

    public void remove(Session session) {
        this.sessionMap.remove(session.getId());
    }

    public int currentCount() {
        return this.sessionMap.size();
    }

    public void sendMessageToAll(String message) {
        Lists.newArrayList(this.sessionMap.values()).forEach(session -> {
            sendMessage(session, message);
        });
    }

    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
