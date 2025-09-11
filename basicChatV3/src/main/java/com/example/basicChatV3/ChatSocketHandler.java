package com.example.basicChatV3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Set<String> bannedUsers = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<WebSocketSession, String> userNames = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        userNames.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        // handle registration
        if ("register".equalsIgnoreCase(chatMessage.getType())) {
            userNames.put(session, chatMessage.getFrom());
            session.sendMessage(new TextMessage("{\"system\":\"Registered as " + chatMessage.getFrom() + "\"}"));
        }
        // handle chat
        else if ("chat".equalsIgnoreCase(chatMessage.getType())) {
            for (WebSocketSession s : sessions) {
                if (!s.isOpen()) continue;

                String username = userNames.get(s);
                if (username != null && bannedUsers.contains(username)) continue; // skip banned users

                s.sendMessage(new TextMessage(message.getPayload())); // broadcast
            }
        }
        // handle ban command (optional)
        else if ("ban".equalsIgnoreCase(chatMessage.getType())) {
            bannedUsers.add(chatMessage.getTo()); // add user to banned list
        }
        // handle unban command (optional)
        else if ("unban".equalsIgnoreCase(chatMessage.getType())) {
            bannedUsers.remove(chatMessage.getTo()); // remove user from banned list
        }
    }
}
