package com.lknmproduction.messengerrest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lknmproduction.messengerrest.domain.utils.UserChat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 4096)
    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(
            mappedBy = "chat",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<UserChat> userList;

    @OneToMany(
            mappedBy = "chat",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Message> messageList;

}
