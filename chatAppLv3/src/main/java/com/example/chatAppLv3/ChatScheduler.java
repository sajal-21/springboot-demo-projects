package com.example.chatAppLv3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatScheduler {

    @Autowired
    private ChatService chatService;

    @Scheduled(fixedRate = 20000)
    public void sendSystemMessage() throws Exception {
        String msg = "[System Notice] Keep the chat active!";
        chatService.broadcastSystemMessage(msg);
        System.out.println("Scheduler ran: " + msg);
    }

}

