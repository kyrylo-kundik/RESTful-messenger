package com.lknmproduction.messengerrest.service;

import com.lknmproduction.messengerrest.domain.User;

import java.util.List;

public interface UserService {

    public User saveUser(User user);

    public User findUserById(Long id);

    public List<User> findUsers();

    public User updateUser(User user);

    public User deleteUserById(Long id);

}
