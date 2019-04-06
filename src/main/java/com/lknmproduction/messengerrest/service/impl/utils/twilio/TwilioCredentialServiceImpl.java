package com.lknmproduction.messengerrest.service.impl.utils.twilio;

import com.lknmproduction.messengerrest.service.utils.twilio.TwilioCredentialService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:twillioCredentials/env.properties")
public class TwilioCredentialServiceImpl implements TwilioCredentialService {

    @Value("${twilioAccountSid}")
    private String twilioAccountSid;
    @Value("${twilioApiKey}")
    private String twilioApiKey;
    @Value("${twilioApiSecret}")
    private String twilioApiSecret;
    @Value("${serviceSid}")
    private String serviceSid;
    @Value("${authToken}")
    private String authToken;
    @Value("${messagingServiceSid}")
    private String messageService;

    @Override
    public String getTwilioAccountSid() {
        return twilioAccountSid;
    }

    @Override
    public String getTwilioApiKey() {
        return twilioApiKey;
    }

    @Override
    public String getTwilioApiSecret() {
        return twilioApiSecret;
    }

    @Override
    public String getServiceSid() {
        return serviceSid;
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String getMessagingService() {
        return messageService;
    }
}
