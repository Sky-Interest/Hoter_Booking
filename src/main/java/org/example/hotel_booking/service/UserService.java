package org.example.hotel_booking.service;

import org.example.hotel_booking.pojo.User;
import org.example.hotel_booking.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
            throw new RuntimeException("用户已存在！");
        }
        // 禁止注册为 OTHER 角色
        if (newUser.getRole() == User.Role.OTHER) {
            throw new RuntimeException("不能注册为 已离职（OTHER） 角色");
        }
        // 对密码进行加密
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        // 保存新用户到数据库
        return userRepo.save(newUser);
    }

    public User loginUser(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("未找到该用户！");
        }
        // 检查用户是否为 OTHER 角色
        if (user.getRole() == User.Role.OTHER) {
            throw new RuntimeException("已离职（OTHER）用户不能登录！");
        }
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误！");
        }
        return user;
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("未找到该用户id：" + id));
        // 如果修改了密码，则需要重新加密
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        }
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setRole(userDetails.getRole());
        return userRepo.save(user);
    }
    public User findUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }


    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("未找到该用户id： " + id));
        user.setRole(User.Role.OTHER); // 将角色改为 OTHER
        userRepo.save(user); // 保存修改
    }

    public User disableUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        user.setRole(User.Role.OTHER);
        return userRepo.save(user);
    }

    public User enableUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        // 你需要根据你的业务逻辑决定启用用户时将其设置为哪个角色，这里假设默认为 USER
        user.setRole(User.Role.USER);
        return userRepo.save(user);
    }
}
