package com.example.chatAppLv3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        chatService.registerSession("anonymous", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatService.removeSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessageDto chatMessageDto = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);

        String content = chatMessageDto.getContent();
        String user = chatMessageDto.getUser();

        if ("register".equals(content)) {
            chatService.registerSession(user, session);
        } else {
            if ("all".equals(user)) {
                chatService.broadcastMessage(chatMessageDto, session);
            } else {
                chatService.sendPrivateMessage(chatMessageDto, session);
            }
        }
    }

}
