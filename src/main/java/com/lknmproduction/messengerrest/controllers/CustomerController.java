package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.domain.Customer;
import com.lknmproduction.messengerrest.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CustomerController.BASE_URL)
public class CustomerController {

    public static final String BASE_URL = "/api/v1/customers";

    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerController(CustomerService customerService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerService = customerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signUp")
    public void signUp(@RequestBody Customer customer) {
        customer.setPass(bCryptPasswordEncoder.encode(customer.getPass()));
        customerService.saveCostumer(customer);
    }

    @GetMapping
    public List<Customer> getAllCostumers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCostumerById(@PathVariable Long id) {
        return customerService.findCostumerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCostumer(customer);
    }
}
