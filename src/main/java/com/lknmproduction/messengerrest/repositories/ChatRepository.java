package com.lknmproduction.messengerrest.repositories;

import com.lknmproduction.messengerrest.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN c.userList u WHERE u.id = ?1")
    List<Chat> getAllByUser(Long userId);

}
