package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.domain.Chat;
import com.lknmproduction.messengerrest.repositories.ChatRepository;
import com.lknmproduction.messengerrest.service.ChatService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"chats"})
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public void updateChat(Chat chat) {
        chatRepository.save(chat);
    }

    @Override
    public Chat deleteChat(Long id) {

        Chat chat = this.findChat(id);

        chatRepository.deleteById(id);

        return chat;
    }

    @Override
    @Cacheable
    public Chat findChat(Long id) {

        Optional optional = chatRepository.findById(id);

        if (optional.isPresent())
            return (Chat) optional.get();

        return null;
    }

    @Override
    @Cacheable
    public List<Chat> getChats() {
        return chatRepository.findAll();
    }

    @Override
    public List<Chat> findUserChat(Long id) {
        return chatRepository.getAllByUser(id);
    }
}
