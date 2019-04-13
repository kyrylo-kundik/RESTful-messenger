package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.repositories.UserRepository;
import com.lknmproduction.messengerrest.service.NotificationService;
import com.lknmproduction.messengerrest.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@CacheConfig(cacheNames = {"users"})
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public UserServiceImpl(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Cacheable
    public User findUserById(Long id) {
        User user = null;
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent())
            user = optional.get();
        return user;
    }

    @Override
    @Cacheable
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
    public List<Device> userDevicesByPhoneNumber(String phoneNumber) {
        User user = userRepository.findFirstByPhoneNumber(phoneNumber);
        if (user != null)
            return user.getDeviceList();
        return null;
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findFirstByPhoneNumber(phoneNumber);
    }

    @Override
    public List<User> findUserByPhoneNumberLike(String phoneNumber) {
        return userRepository.findAllByPhoneNumberContains(phoneNumber);
    }

    @Override
    public void sendNotifications(String title, String body, List<String> phoneNumbers) {
        List<Device> deviceList = phoneNumbers
                .stream()
                .map(this::userDevicesByPhoneNumber)
                .flatMap(Collection::stream)
                .filter(Device::getIsActive)
                .collect(Collectors.toList());

        notificationService.sendNotifications(title, body, deviceList
                .stream()
                .map(Device::getPushId)
                .collect(Collectors.toList()));
    }
}
