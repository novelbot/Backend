package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.user.UserCreateRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    // private final UserService userService;

    // public UserController(UserService userService) {
    //     this.userService = userService;
    // }

    // @PostMapping
    // public ResponseEntity<Void> signup(@RequestBody UserCreateRequest userCreate) {
    //     userService.createUser(userCreate);
    //     return ResponseEntity.status(HttpStatus.CREATED).build();
    // }

    // @DeleteMapping("/{userId}")
    // public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
    //     userService.deleteUser(userId);
    //     return ResponseEntity.noContent().build();
    // }
}