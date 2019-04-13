package com.lknmproduction.messengerrest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lknmproduction.messengerrest.domain.utils.UserChat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String username;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "photo_url")
    private String photoUrl;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_seen")
    private Date lastSeen;
    @Column(length = 4096)
    private String bio;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(name = "user_to_device",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "device_id")}
    )
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Device> deviceList;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<UserChat> chatList;

}
