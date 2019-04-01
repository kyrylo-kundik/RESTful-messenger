package customerRestExample.bootstrap;

import customerRestExample.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    public BootstrapData(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {

//        System.out.println("Loading Customer Data");
//
//        Customer c1 = new Customer();
//        c1.setFirstname("Michale");
//        c1.setLastname("Weston");
//        customerRepository.save(c1);
//
//        Customer c2 = new Customer();
//        c2.setFirstname("John");
//        c2.setLastname("Wick");
//        customerRepository.save(c2);
//
//        Customer c3 = new Customer();
//        c3.setFirstname("James");
//        c3.setLastname("Born");
//        customerRepository.save(c3);
//
//        System.out.println("Customers Saved: " + customerRepository.count());

    }
}
