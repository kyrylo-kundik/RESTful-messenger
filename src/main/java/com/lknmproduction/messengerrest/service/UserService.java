package com.lknmproduction.messengerrest.service;

import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    public User saveUser(User user);

    public User findUserById(Long id);

    public List<User> findUsers();

    public User updateUser(User user);

    public User deleteUserById(Long id);

    public List<Device> userDevicesByPhoneNumber(String phoneNumber);

    public User findUserByPhoneNumber(String phoneNumber);

    public List<User> findUserByPhoneNumberLike(String phoneNumber);

    CompletableFuture<String> sendNotifications(String title, String body, String payload, List<String> phoneNumbers);

    List<String> getPushIdsByPhoneNumbers(List<String> phoneNumbers);

}
