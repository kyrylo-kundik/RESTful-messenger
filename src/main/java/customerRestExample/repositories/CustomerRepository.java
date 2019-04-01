package customerRestExample.repositories;

import customerRestExample.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer getFirstByLastnameEquals(String lastname);
}
