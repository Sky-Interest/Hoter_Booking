package org.example.hotel_booking.service;

import org.example.hotel_booking.pojo.User;
import org.example.hotel_booking.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User registerUser(User newUser) {
        // 检查用户名是否已存在
        Optional<User> existingUser = Optional.ofNullable(userRepo.findByUsername(newUser.getUsername()));
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists.");
        }
        // 对密码进行加密
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        // 保存新用户到数据库
        return userRepo.save(newUser);
    }
    // 在UserService中添加登录方法
    public User loginUser(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password is incorrect.");
        }
        return user;
    }


    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }
}
