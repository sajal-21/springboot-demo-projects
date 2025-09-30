package com.example.chatAppLv3;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMessageDto {

    private String content;

    private String user;

}
