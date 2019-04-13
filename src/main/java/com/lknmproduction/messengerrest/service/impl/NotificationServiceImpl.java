package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    public void sendNotifications(String title, String body, List<String> pushIds) {

    }

}
