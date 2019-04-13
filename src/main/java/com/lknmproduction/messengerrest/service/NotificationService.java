package com.lknmproduction.messengerrest.service;

import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    CompletableFuture<String> sendNotifications(String title, String body, List<String> pushIds) throws IOException, JSONException;

}
