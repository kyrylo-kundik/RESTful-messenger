package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.service.UserService;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.ChatGrant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/api/v1/user";
    private final UserService userService;
    @Value("${twilioAccountSid}")
    private String twilioAccountSid;
    @Value("${twilioApiKey")
    private String twilioApiKey;
    @Value("${twilioApiSecret")
    private String twilioApiSecret;
    @Value("${serviceSid}")
    private String serviceSid;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public User deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findUsers();
    }

    @GetMapping("/chatToken")
    public String getChatToken(@RequestHeader("Authorization") String jwtTokenUser) {

        ChatGrant grant = new ChatGrant();
        grant.setServiceSid(serviceSid);

        AccessToken token = new AccessToken.Builder(twilioAccountSid, twilioApiKey, twilioApiSecret)
                .identity(jwtTokenUser).grant(grant).build();

        return token.toJwt();
    }

}
