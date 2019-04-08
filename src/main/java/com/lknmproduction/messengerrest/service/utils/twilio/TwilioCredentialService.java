package com.lknmproduction.messengerrest.service.utils.twilio;

public interface TwilioCredentialService {

    String getTwilioAccountSid();

    String getTwilioApiKey();

    String getTwilioApiSecret();

    String getServiceSid();

    String getAuthToken();

    String getMessagingService();
}
