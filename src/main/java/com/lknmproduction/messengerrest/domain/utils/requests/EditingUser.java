package com.lknmproduction.messengerrest.domain.utils.requests;

import lombok.Data;

import java.util.Date;

@Data
public class EditingUser {

    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private Date lastSeen;
    private String bio;

}
