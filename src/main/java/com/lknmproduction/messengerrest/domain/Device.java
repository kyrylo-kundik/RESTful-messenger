package com.lknmproduction.messengerrest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

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
    @ManyToMany(mappedBy = "deviceList", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> user;

}
