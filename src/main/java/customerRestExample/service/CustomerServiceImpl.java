package customerRestExample.service;

import customerRestExample.domain.Customer;
import customerRestExample.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findCostumerById(Long id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer saveCostumer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByLastname(String lastname) {
        return customerRepository.getFirstByLastnameEquals(lastname);
    }
}
