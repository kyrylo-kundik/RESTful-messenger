package com.lknmproduction.messengerrest.service;

import java.util.List;

public interface NotificationService {

    void sendNotifications(String title, String body, List<String> pushIds);

}
