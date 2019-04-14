package com.lknmproduction.messengerrest.domain.utils.requests;

import lombok.Data;

import java.util.List;

@Data
public class SendNotifBody {
    private String title;
    private String body;
    private List<String> phoneList;
    private String payload;
}
