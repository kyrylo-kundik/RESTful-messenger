package com.lknmproduction.messengerrest.service.redis;

public interface MessagePublisher {

    void publish(final String message);

}
