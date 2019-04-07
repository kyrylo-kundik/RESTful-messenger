package com.lknmproduction.messengerrest.service.utils.twilio;

public interface TwilioService {

    public void sendMessage(final String to, final String body);

    public String getChatToken(String phoneNumber);

}
