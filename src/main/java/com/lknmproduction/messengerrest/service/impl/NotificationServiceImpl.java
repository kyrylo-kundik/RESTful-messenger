package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotifications(String title, String body, List<String> pushIds) {

    }

}
