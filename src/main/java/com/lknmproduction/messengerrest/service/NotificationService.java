package com.lknmproduction.messengerrest.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    CompletableFuture<String> sendNotifications(String title, String body, String payLoad, List<String> pushIds);

}
