package com.inventory.service;

import com.inventory.entity.Role;
import com.inventory.entity.User;
import com.inventory.repository.RoleRepository;
import com.inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("STARTING DATA SYNC...");

        // 1. Ensure Roles exist with known state
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ADMIN");
            return roleRepository.save(r);
        });

        if (roleRepository.findByName("STAFF").isEmpty()) {
            Role r = new Role();
            r.setName("STAFF");
            roleRepository.save(r);
        }

        // 2. FORCE RECREATE ADMIN (To be 100% sure password/role matches)
        userRepository.findByUsername("admin").ifPresent(user -> {
            userRepository.delete(user);
            userRepository.flush();
            System.out.println("CLEANUP: Old admin removed.");
        });

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@inventory.com");
        admin.setFullName("System Admin");
        admin.setRole(adminRole);
        admin.setEnabled(true);

        userRepository.save(admin);
        System.out.println("SUCCESS: New Admin user created -> admin / admin123");
        System.out.println("Ready on port 9090");
    }
}
