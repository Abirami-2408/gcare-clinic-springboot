package com.gcare.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gcare.entity.AdminUser;
import com.gcare.repository.AdminUserRepository;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    // the default text that means "the password was never set"; do NOT change this to your real password
    private static final String PLACEHOLDER = "YOUR_ADMIN_PASSWORD";

    private final AdminUserRepository repo;
    private final PasswordEncoder encoder;

    @Value("${app.admin.username}")
    private String username;

    @Value("${app.admin.password}")
    private String password;

    public AdminSeeder(AdminUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        if (username == null || username.isBlank()) {
            log.warn("No admin created: app.admin.username is EMPTY");
            return;
        }
        if (password == null || password.isBlank()) {
            log.warn("No admin created: app.admin.password is EMPTY");
            return;
        }
        if (PLACEHOLDER.equals(password)) {
            log.warn("No admin created: app.admin.password is still the placeholder text");
            return;
        }

        if (repo.findByUsername(username).isPresent()) {
            log.info("Admin user '{}' already exists, nothing to do", username);
            return;
        }

        AdminUser admin = new AdminUser();
        admin.setUsername(username);
        admin.setPassword(encoder.encode(password));   // only the BCrypt hash is stored
        repo.save(admin);

        log.info("Admin user '{}' created", username);
    }
}