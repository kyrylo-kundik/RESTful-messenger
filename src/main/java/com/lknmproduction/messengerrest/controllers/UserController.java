package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.service.utils.TwilioCredentialService;
import com.lknmproduction.messengerrest.service.UserService;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.ChatGrant;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/api/v1/user";
    private final UserService userService;
    private final TwilioCredentialService twilioCredentialService;

    public UserController(UserService userService, TwilioCredentialService twilioCredentialService) {
        this.userService = userService;
        this.twilioCredentialService = twilioCredentialService;
    }

//    @PostMapping("/signUp")
//    public void signUp(@RequestBody User user) {
//        user.setPassHash(bCryptPasswordEncoder.encode(user.getPassHash()));
//        userService.saveUser(user);
//    }

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

    @PostMapping("/createUser")
    @ResponseBody
    public User createUser(@RequestBody User user) {

        return user;
    }

    @GetMapping("/chatToken")
    public String getChatToken(@RequestHeader("Authorization") String jwtTokenUser) {

        ChatGrant grant = new ChatGrant();
        grant.setServiceSid(twilioCredentialService.getServiceSid());

        AccessToken token = new AccessToken.Builder(twilioCredentialService.getTwilioAccountSid(),
                twilioCredentialService.getTwilioApiKey(), twilioCredentialService.getTwilioApiSecret())
                .identity(jwtTokenUser).grant(grant).build();

        return token.toJwt();
    }

}
