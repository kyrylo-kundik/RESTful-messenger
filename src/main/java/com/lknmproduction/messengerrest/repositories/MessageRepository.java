package com.lknmproduction.messengerrest.repositories;

import com.lknmproduction.messengerrest.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> getAllByChat_Id(Long chatId);

}
