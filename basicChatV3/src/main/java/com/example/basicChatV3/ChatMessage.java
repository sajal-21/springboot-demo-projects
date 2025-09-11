package com.example.basicChatV3;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ChatMessage {

    private String type;
    private String from;
    private String to;
    private String message;
}