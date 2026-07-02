package com.project.campus.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketConnectionHandler extends TextWebSocketHandler {

    // userId -> session
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    // sessionId -> userId
    private final Map<String, Long> sessionUsers = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Long userId = extractUserId(session);

        userSessions.put(userId, session);
        sessionUsers.put(session.getId(), userId);

        System.out.println("User " + userId + " Connected");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws Exception {

        Long userId = sessionUsers.remove(session.getId());

        if (userId != null) {
            userSessions.remove(userId);
        }

        System.out.println("User " + userId + " Disconnected");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {

        System.out.println("Received : " + message.getPayload());

        // Echo back only to sender (optional)
        session.sendMessage(new TextMessage("Server : " + message.getPayload()));
    }

    /**
     * Send notification to one user.
     */
    public void sendToUser(Long userId, String message) {

        WebSocketSession session = userSessions.get(userId);

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Broadcast to every connected user.
     */
    public void broadcast(String message) {

        for (WebSocketSession session : userSessions.values()) {

            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Check if a user is online.
     */
    public boolean isUserOnline(Long userId) {
        return userSessions.containsKey(userId);
    }

    /**
     * Get connected users count.
     */
    public int onlineUsers() {
        return userSessions.size();
    }

    /**
     * Extract userId from:
     * ws://localhost:8080/hello?userId=5
     */
    private Long extractUserId(WebSocketSession session) {

        String query = session.getUri().getQuery();

        if (query == null || !query.startsWith("userId=")) {
            throw new RuntimeException("Missing userId in websocket URL");
        }

        return Long.parseLong(query.substring("userId=".length()));
    }
}