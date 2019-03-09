package com.lknmproduction.messengerrest.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String username;
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "pass_hash")
    private String passHash;
    @Column(name = "dynamic_salt")
    private String dynamicSalt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_seen")
    private Date lastSeen;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_fk")
    private List<Device> deviceList;

}
