package com.example.authentication.model;

import com.example.authentication.remote.RemoteDataService;
import com.example.authentication.remote.RemoteUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final RemoteDataService remoteDataService;

    @GetMapping()
    @ResponseBody
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping()
    public User createUser(@RequestBody() User user) {

        log.info("creating user " + user.toString());
        User us = new User();
        us.setUsername(user.getUsername());
        return userRepository.save(us);
    }

    @PostMapping(value = "/login")
    public boolean userToken(@RequestBody() User user) {
        User existing = userRepository.findUserByUsername(user.getUsername());
        return existing.getPassword().equals(user.getPassword());
    }

    @GetMapping("/remote")
    public List<RemoteUserDto> getRemoteUsers() {
        return remoteDataService.getRemoteUser();
    }
}
