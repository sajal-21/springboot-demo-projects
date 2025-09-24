package com.example.chatAppLv1;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMessageDto {

    @NonNull
    private String type;

    @NonNull
    private String sender;

    @NonNull
    private String receiver;

    @NonNull
    private String content;

}
