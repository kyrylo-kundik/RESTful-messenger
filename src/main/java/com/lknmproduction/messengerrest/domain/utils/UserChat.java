package com.lknmproduction.messengerrest.domain.utils;

import com.lknmproduction.messengerrest.domain.Chat;
import com.lknmproduction.messengerrest.domain.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user_to_chat")
public class UserChat {

    @EmbeddedId
    private UserChatId id;

    @ManyToOne
    @JoinColumn(name = "fk_user", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_chat", insertable = false, updatable = false)
    private Chat chat;

    @Column(name = "last_consumed_message_index")
    private int lastConsumed;

    @Embeddable
    @Data
    public static class UserChatId implements Serializable {

        @Column(name = "fk_user")
        protected Long userId;

        @Column(name = "fk_chat")
        protected Long chatId;

    }

}
