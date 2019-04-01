package customerRestExample.service;

import customerRestExample.domain.Customer;

import java.util.List;

public interface CustomerService {

    Customer findCostumerById(Long id);

    List<Customer> findAllCustomers();

    Customer saveCostumer(Customer customer);

    Customer findByLastname(String lastname);

}
