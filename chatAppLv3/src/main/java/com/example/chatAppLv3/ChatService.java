package com.example.chatAppLv3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    private final Map<String, Set<WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    private final Map<WebSocketSession, String> sessionToUser = new ConcurrentHashMap<>();

    public void registerSession(String username, WebSocketSession session) {
        removeSession(session);

        if (username == null || username.trim().isEmpty() || "anonymous".equalsIgnoreCase(username)) {
            username = "anonymous-" + session.getId().substring(0, 6);
        }

        userSessions.computeIfAbsent(username, k -> ConcurrentHashMap.newKeySet()).add(session);
        sessionToUser.put(session, username);

        System.out.println("Registered session as: " + username);
    }

    public void removeSession(WebSocketSession session) {
        String username = sessionToUser.remove(session);

        if (username != null) {
            Set<WebSocketSession> sessions = userSessions.get(username);

            if (sessions != null) {
                sessions.remove(session);

                if (sessions.isEmpty()) {
                    userSessions.remove(username);
                }
            }
        }
    }

    public void broadcastMessage(ChatMessageDto chatMessageDto, WebSocketSession session) throws Exception {
        String userName = getUsername(session);
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setSender(userName);
        chatMessage.setReceiver(chatMessageDto.getUser());
        chatMessage.setContent(chatMessageDto.getContent());

        chatRepository.save(chatMessage);

        for (Set<WebSocketSession> sessions : userSessions.values()) {
            for (WebSocketSession s : sessions) {
                if (s != null && s.isOpen()) {
                    s.sendMessage(new TextMessage(chatMessage.getSender() + " (to all): " + chatMessage.getContent()));
                }
            }
        }
    }

    public void sendPrivateMessage(ChatMessageDto chatMessageDto, WebSocketSession session) throws Exception {
        String userName = getUsername(session);
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setSender(userName);
        chatMessage.setReceiver(chatMessageDto.getUser());
        chatMessage.setContent(chatMessageDto.getContent());

        chatRepository.save(chatMessage);

        Set<WebSocketSession> sessions = userSessions.get(chatMessage.getReceiver());

        for (WebSocketSession s : sessions) {
            if (s != null && s.isOpen()) {
                s.sendMessage(new TextMessage(chatMessage.getSender() + " (to you): " + chatMessage.getContent()));
            }
        }
    }

    public void broadcastSystemMessage(String msg) throws Exception {
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setSender("system");
        chatMessage.setReceiver("all");
        chatMessage.setContent(msg);

        chatRepository.save(chatMessage);

        for (Set<WebSocketSession> sessions : userSessions.values()) {
            for (WebSocketSession s : sessions) {
                if (s != null && s.isOpen()) {
                    s.sendMessage(new TextMessage(chatMessage.getSender() + " (to all): " + chatMessage.getContent()));
                }
            }
        }
    }

    private String getUsername(WebSocketSession session) {
        return sessionToUser.getOrDefault(session, "");
    }

}
