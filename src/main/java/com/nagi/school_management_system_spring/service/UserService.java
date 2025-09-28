package com.nagi.school_management_system_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.UserTypeEnum;
import com.nagi.school_management_system_spring.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }
    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public UserModel registerUser(UserModel registerUser) {
        if (userRepository.existsByEmail(registerUser.getEmail())) {
            throw new RuntimeException("Email already in use: " + registerUser.getEmail());
        }

        return userRepository.save(registerUser);
    }

    public UserModel loginUser(String email, String password) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password for user: " + email);
        }

        return userRepository.save(user);
    }

    public UserModel updateProfileById(Long userId, UserModel userUpdate) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // arrumar futuramente as verificações usar (MapStruct)
        if (userUpdate.getName() != null && !userUpdate.getName().isBlank()) {
            user.setName(userUpdate.getName());
        }
        if (userUpdate.getEmail() != null && !userUpdate.getEmail().isBlank()) {
            user.setEmail(userUpdate.getEmail());
        }
        if (userUpdate.getPhoneNumber() != null && !userUpdate.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(userUpdate.getPhoneNumber());
        }
        if (userUpdate.getCpf() != null && !userUpdate.getCpf().isBlank()) {
            user.setCpf(userUpdate.getCpf());
        }
        if (userUpdate.getAddress() != null && !userUpdate.getAddress().isBlank()) {
            user.setAddress(userUpdate.getAddress());
        }
        if (userUpdate.getBirthDate() != null) {
            user.setBirthDate(userUpdate.getBirthDate());
        }
        if (userUpdate.getIsActive() != null) {
            user.setIsActive(userUpdate.getIsActive());
        }
        if (userUpdate.getUsertype() != null) {
            user.setUsertype(userUpdate.getUsertype());
        }

        if (userUpdate.getPassword() != null && !userUpdate.getPassword().isBlank()) {
            user.setPassword(userUpdate.getPassword());
        }

        return userRepository.save(user);
    }

    public UserModel changePasswordByEmail(String email, UserModel updatePassword) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (updatePassword.getPassword() != null && !updatePassword.getPassword().isBlank()) {
            user.setPassword(updatePassword.getPassword());
        }

        return userRepository.save(user);
    }

    public List<UserModel> listByType(UserTypeEnum usertype) {
        return userRepository.findByUsertype(usertype);

    }

    public void activateUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        user.setIsActive(true);
        userRepository.save(user);

    }

    public void deactivateUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        user.setIsActive(false);
        userRepository.save(user);
    }
}
