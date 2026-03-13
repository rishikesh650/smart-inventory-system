package com.inventory.service;

import com.inventory.dto.UserDTO;
import com.inventory.entity.Role;
import com.inventory.entity.User;
import com.inventory.repository.RoleRepository;
import com.inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Role userRole = roleRepository.findByName("STAFF").orElseGet(() -> {
            Role role = new Role();
            role.setName("STAFF");
            return roleRepository.save(role);
        });

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setRole(userRole);
        user.setEnabled(true);

        userRepository.save(user);
    }
}
