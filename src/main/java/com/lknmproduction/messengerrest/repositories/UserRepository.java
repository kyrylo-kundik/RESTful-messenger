package com.lknmproduction.messengerrest.repositories;

import com.lknmproduction.messengerrest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
