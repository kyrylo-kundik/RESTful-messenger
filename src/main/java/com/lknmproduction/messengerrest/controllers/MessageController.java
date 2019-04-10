package com.lknmproduction.messengerrest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MessageController.BASE_URL)
public class MessageController {

    public static final String BASE_URL = "/api/v1/message";



}
