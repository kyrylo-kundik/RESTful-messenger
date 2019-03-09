package com.lknmproduction.messengerrest.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "push_id")
    private Long pushId;

}
