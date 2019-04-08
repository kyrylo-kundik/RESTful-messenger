package com.lknmproduction.messengerrest.service.impl.utils.twilio;

import com.lknmproduction.messengerrest.service.utils.twilio.TwilioCredentialService;
import com.lknmproduction.messengerrest.service.utils.twilio.TwilioService;
import com.twilio.Twilio;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.ChatGrant;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class TwilioServiceImpl implements TwilioService {

    private final TwilioCredentialService twilioCredentialService;

    public TwilioServiceImpl(TwilioCredentialService twilioCredentialService) {
        this.twilioCredentialService = twilioCredentialService;
    }

    @Override
    public void sendMessage(String to, String body) {
        Twilio.init(twilioCredentialService.getTwilioAccountSid(), twilioCredentialService.getAuthToken());
//        ValidationRequest validationRequest = ValidationRequest.creator(
//                new com.twilio.type.PhoneNumber(to))
//                .setFriendlyName(to)
//                .create();
//        System.out.println("Friendly Name:");
//        System.out.println(validationRequest.getFriendlyName());
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(twilioCredentialService.getMessagingService()),
                body)
                .create();
        System.out.println("Message Sid:");
        System.out.println(message.getSid());
    }

    @Override
    public String getChatToken(String phoneNumber) {
        ChatGrant grant = new ChatGrant();
        grant.setServiceSid(twilioCredentialService.getServiceSid());

        AccessToken token = new AccessToken.Builder(twilioCredentialService.getTwilioAccountSid(),
                twilioCredentialService.getTwilioApiKey(), twilioCredentialService.getTwilioApiSecret())
                .identity(phoneNumber).grant(grant).build();

        return token.toJwt();
    }
}
