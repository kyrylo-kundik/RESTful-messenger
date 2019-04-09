package com.lknmproduction.messengerrest.service.impl.security;

import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User appUser = userService.findUserByPhoneNumber(s);
        if (appUser == null)
            throw new UsernameNotFoundException(s);
        return new org.springframework.security.core.userdetails.User(appUser.getPhoneNumber(), appUser.getPhoneNumber(), Collections.emptyList());
    }
}
