package com.lknmproduction.messengerrest.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = "user")
@Entity
@Table
public class Device {

    @Id
    private String id;
    @Column(name = "push_id")
    private Long pushId;
    @Column(name = "is_active")
    private Boolean isActive;
    @ManyToOne
    private User user;

}
