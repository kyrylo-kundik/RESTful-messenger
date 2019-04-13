package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.domain.Chat;
import com.lknmproduction.messengerrest.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ChatController.BASE_URL)
public class ChatController {

    public static final String BASE_URL = "/api/v1/chat";

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Chat createChat(@RequestBody Chat chat) {
        return chatService.createChat(chat);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void updateChat(@RequestBody Chat chat, @PathVariable Long id) {

        Chat chat1 = chatService.findChat(id);

        chat1.setDescription(chat.getDescription());
        chat1.setPhotoUrl(chat.getPhotoUrl());
        chat1.setTitle(chat.getTitle());

        chatService.updateChat(chat1);

    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Chat deleteChat(@PathVariable Long id) {
        return chatService.deleteChat(id);
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Chat> getAllChats() {
        return chatService.getChats();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Chat getChat(@PathVariable Long id) {
        return chatService.findChat(id);
    }

    @GetMapping("/getUserChat/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Chat> getUserChats(@PathVariable Long id) {
        return chatService.findUserChat(id);
    }

}
