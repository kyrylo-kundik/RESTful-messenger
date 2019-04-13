package com.lknmproduction.messengerrest.service;

import com.lknmproduction.messengerrest.domain.Chat;

import java.util.List;

public interface ChatService {

    Chat createChat(Chat chat);

    void updateChat(Chat chat);

    Chat deleteChat(Long id);

    Chat findChat(Long id);

    List<Chat> getChats();

    List<Chat> findUserChat(Long id);
}
