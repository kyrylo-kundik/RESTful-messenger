package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.domain.Message;
import com.lknmproduction.messengerrest.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(MessageController.BASE_URL)
public class MessageController {

    public static final String BASE_URL = "/api/v1/message";

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Message createMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void updateMessage(@RequestBody Message message, @PathVariable Long id) {

        Message message1 = messageService.findById(id);

        message1.setText(message.getText());
        message1.setEdited(true);

        messageService.updateMessage(message1);

    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Message deleteMessage(@PathVariable Long id) {
        return messageService.deleteMessage(id);
    }

    @GetMapping("/getByChat/{chatId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Message> getAllmessages(@PathVariable Long chatId) {
        return messageService.getAllByChatId(chatId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Message getMessage(@PathVariable Long id) {
        return messageService.findById(id);
    }

    @GetMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Message editMessage(@PathVariable Long id, @RequestBody Message message) {
        Message message1 = messageService.findById(id);

        message1.setText(message.getText());
        message1.setEdited(true);

        messageService.editMessage(message1);

        return message1;
    }

}
