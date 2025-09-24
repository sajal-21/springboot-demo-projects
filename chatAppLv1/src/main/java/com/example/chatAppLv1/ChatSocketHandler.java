package com.example.chatAppLv1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        userSessions.values().remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        ChatMessageDto chatMessageDto;
        try {
            chatMessageDto = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);
        } catch (Exception e) {
            session.sendMessage(new TextMessage("{\"error\":\"invalid message format\"}"));
            return;
        }

        String chatType = chatMessageDto.getType();

        if("register".equalsIgnoreCase(chatType)) {
            userSessions.put(chatMessageDto.getSender(), session);
        }
        else if ("broadcast".equalsIgnoreCase(chatType)){
            for (Map.Entry<String, WebSocketSession> entry : userSessions.entrySet()) {
                WebSocketSession s = entry.getValue();
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(message.getPayload()));
                    chatService.saveChat(chatMessageDto);
                } else {
                    session.sendMessage(new TextMessage("{\"error\":\"invalid message format\"}"));
                }
            }
        }
        else if ("chat".equalsIgnoreCase(chatType)) {
            WebSocketSession s = userSessions.get(chatMessageDto.getReceiver());
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(message.getPayload()));
                chatService.saveChat(chatMessageDto);
            } else {
                session.sendMessage(new TextMessage("{\"error\":\"invalid message format\"}"));
            }
        }
    }

}
