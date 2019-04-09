package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.repositories.UserRepository;
import com.lknmproduction.messengerrest.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Cacheable("student")
    public User findUserById(Long id) {
        User user = null;
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent())
            user = optional.get();
        return user;
    }

    @Override
    @Cacheable("student")
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User deleteUserById(Long id) {
        User user = findUserById(id);
        if (user != null)
            userRepository.deleteById(id);
        return user;
    }

    @Override
    @Cacheable("student")
    public List<Device> userDevicesByPhoneNumber(String phoneNumber) {
        User user = userRepository.findFirstByPhoneNumber(phoneNumber);
        if (user != null)
            return user.getDeviceList();
        return null;
    }

    @Override
    @Cacheable("student")
    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findFirstByPhoneNumber(phoneNumber);
    }

    @Override
    @Cacheable("student")
    public List<User> findUserByPhoneNumberLike(String phoneNumber) {
        return userRepository.findAllByPhoneNumberContains(phoneNumber);
    }
}
