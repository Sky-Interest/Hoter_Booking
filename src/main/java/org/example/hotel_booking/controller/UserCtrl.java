package org.example.hotel_booking.controller;

import org.example.hotel_booking.pojo.User;
import org.example.hotel_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/api")
public class UserCtrl {
    private final UserService userService;

    @Autowired
    public UserCtrl(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }
    // 在UserCtrl中添加登录端点
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(loggedInUser);
    }

//    @GetMapping("/{username}")
//    public User getUserByUsername(@PathVariable String username) {
//        return userService.findUserByUsername(username);
//    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok(updatedUser);
    }
    @PutMapping("/users/{id}/disable")
    public ResponseEntity<User> disableUser(@PathVariable Long id) {
        User updatedUser = userService.disableUser(id);
        if (updatedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{id}/enable")
    public ResponseEntity<User> enableUser(@PathVariable Long id) {
        User updatedUser = userService.enableUser(id);
        if (updatedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok(updatedUser);
    }

@DeleteMapping("/users/{id}")
public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long id) {
    userService.deleteUser(id);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
}
}

