package com.example.chatApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ChatSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        // remove disconnected user
        userSessions.values().remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        if ("register".equalsIgnoreCase(chatMessage.getType())) {
            // save mapping username -> session
            userSessions.put(chatMessage.getFrom(), session);
            session.sendMessage(new TextMessage("{\"system\":\"registered as " + chatMessage.getFrom() + "\"}"));
        }
        else if ("chat".equalsIgnoreCase(chatMessage.getType())) {
            WebSocketSession recipientSession = userSessions.get(chatMessage.getTo());
            if (recipientSession != null && recipientSession.isOpen()) {
                // send to recipient only
                recipientSession.sendMessage(new TextMessage(message.getPayload()));
            } else {
                // optional: notify sender that recipient not found
                session.sendMessage(new TextMessage("{\"system\":\"User " + chatMessage.getTo() + " not available\"}"));
            }
        }
    }
}
