package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.user.UserCreateRequest;
import com.novelbot.api.service.auth.RegistrationService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RegistrationService registor;

    public UserController(RegistrationService registor) {
        this.registor = registor;
    }

    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody UserCreateRequest userCreate) {
        registor.registerUser(userCreate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // @DeleteMapping("/{userId}")
    // public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
    //     userService.deleteUser(userId);
    //     return ResponseEntity.noContent().build();
    // }
}