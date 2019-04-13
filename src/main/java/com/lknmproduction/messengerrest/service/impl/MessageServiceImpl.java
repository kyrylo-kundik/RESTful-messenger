package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.domain.Message;
import com.lknmproduction.messengerrest.repositories.MessageRepository;
import com.lknmproduction.messengerrest.service.MessageService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@CacheConfig(cacheNames = {"messages"})
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void updateMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    @Cacheable
    public Message findById(Long id) {
        Optional optional = messageRepository.findById(id);

        if (optional.isPresent())
            return (Message) optional.get();

        return null;
    }

    @Override
    public Message deleteMessage(Long id) {

        Message message = this.findById(id);

        messageRepository.deleteById(id);

        return message;
    }

    @Override
    public Message editMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getAllByChatId(Long chatId) {
        return messageRepository.getAllByChat_Id(chatId);
    }
}
