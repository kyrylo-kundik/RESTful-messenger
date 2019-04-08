package com.lknmproduction.messengerrest.repositories;

import com.lknmproduction.messengerrest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findFirstByPhoneNumber(String phoneNumber);

    List<User> findAllByPhoneNumberContains(String phoneNumber);
}
