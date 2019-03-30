package com.lknmproduction.messengerrest.repositories;

import com.lknmproduction.messengerrest.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer getFirstByLastnameEquals(String lastname);
}
