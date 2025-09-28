package com.nagi.school_management_system_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.UserTypeEnum;
import com.nagi.school_management_system_spring.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/emails/{email}")
    public ResponseEntity<UserModel> getUserByEmail(@PathVariable String email) {
        try {
            UserModel user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserModel registerUser) {
        try {
            UserModel user = userService.registerUser(registerUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserModel> loginUser(@RequestParam String email, @RequestParam String password) {
        try {
            UserModel user = userService.loginUser(email, password);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserModel> updateProfile(@PathVariable Long userId, @RequestBody UserModel userUpdate) {
        try {
            UserModel user = userService.updateProfileById(userId, userUpdate);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/change-password/{email}")
    public ResponseEntity<UserModel> changePassword(@PathVariable String email, @RequestBody UserModel updatePassword) {
        try {
            UserModel user = userService.changePasswordByEmail(email, updatePassword);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/type/{usertype}")
    public ResponseEntity<List<UserModel>> listByType(@PathVariable UserTypeEnum usertype) {
        List<UserModel> users = userService.listByType(usertype);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/activate/{email}")
    public ResponseEntity<Void> activateUser(@PathVariable String email) {
        try {
            userService.activateUserByEmail(email);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/deactivate/{email}")
    public ResponseEntity<Void> deactivateUser(@PathVariable String email) {
        try {
            userService.deactivateUserByEmail(email);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
