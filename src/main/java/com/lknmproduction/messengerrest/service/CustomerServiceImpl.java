package com.lknmproduction.messengerrest.service;

import com.lknmproduction.messengerrest.domain.Customer;
import com.lknmproduction.messengerrest.repositories.CustomerRepository;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findCostumerById(Long id) {
        return customerRepository.getOne(id);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }
}
