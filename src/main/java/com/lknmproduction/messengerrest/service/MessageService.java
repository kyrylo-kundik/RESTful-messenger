package com.lknmproduction.messengerrest.service;

import com.lknmproduction.messengerrest.domain.Message;

import java.util.List;

public interface MessageService {

    Message createMessage(Message message);

    void updateMessage(Message message);

    Message findById(Long id);

    Message deleteMessage(Long id);

    Message editMessage(Message message);

    List<Message> getAllByChatId(Long chatId);

}
