package com.example.chatAppLv1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public void saveChat(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(chatMessageDto.getType());
        chatMessage.setSender(chatMessageDto.getSender());
        chatMessage.setReceiver(chatMessageDto.getReceiver());
        chatMessage.setContent(chatMessageDto.getContent());
        chatRepository.save(chatMessage);
    }
}
