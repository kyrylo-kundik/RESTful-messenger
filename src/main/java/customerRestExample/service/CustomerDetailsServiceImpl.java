package customerRestExample.service;

import customerRestExample.domain.Customer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private CustomerService customerService;

    public CustomerDetailsServiceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public UserDetails loadUserByUsername(String lastName) throws UsernameNotFoundException {
        Customer applicationUser = customerService.findByLastname(lastName);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(lastName);
        }
        return new User(applicationUser.getLastname(), applicationUser.getPass(), emptyList());
    }
}
