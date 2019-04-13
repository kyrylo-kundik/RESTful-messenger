package com.lknmproduction.messengerrest.domain.utils.requests;

import lombok.Data;

@Data
public class PhoneDeviceBaseLogin {

    private String phoneNumber;
    private String deviceId;

}
