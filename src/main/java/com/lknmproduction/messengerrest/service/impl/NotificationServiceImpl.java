package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:fcmCredentials/env.properties")
public class NotificationServiceImpl implements NotificationService {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    @Value("${firebaseCredentialsKey}")
    private String firebaseServerKey;

    @Override
    @Async
    public CompletableFuture<String> sendNotifications(String title, String body, String payload, List<String> pushIds) {

        JSONObject reqBody = new JSONObject();
        JSONObject notification = new JSONObject();
        JSONObject data = new JSONObject();


        try {

            if (pushIds.size() > 1)
                reqBody.put("registration_ids", pushIds);
            else if (pushIds.size() == 1)
                reqBody.put("to", pushIds.get(0));
            notification.put("title", title);
            notification.put("body", body);
            notification.put("priority", "high");
            notification.put("content_available", true);
            notification.put("sound", "default");

            data.put("payload", payload);

            reqBody.put("notification", notification);
            reqBody.put("data", data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity<String> request = new HttpEntity<>(reqBody.toString());

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + firebaseServerKey));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FCM_URL, request, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);

    }

}

class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

    private final String headerName;
    private final String headerValue;

    HeaderRequestInterceptor(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpRequest wrapper = new HttpRequestWrapper(request);
        wrapper.getHeaders().set(headerName, headerValue);
        return execution.execute(wrapper, body);
    }
}