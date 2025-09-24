package com.example.chatAppLv1;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_msg")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String type;

    @NonNull
    private String sender;

    @NonNull
    private String receiver;

    @NonNull
    private String content;

}
