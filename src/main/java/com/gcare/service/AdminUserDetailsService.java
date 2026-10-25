package com.gcare.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gcare.entity.AdminUser;
import com.gcare.repository.AdminUserRepository;

@Service
public class AdminUserDetailsService implements UserDetailsService {   // the interface Spring Security calls at login

    private final AdminUserRepository repo;

    public AdminUserDetailsService(AdminUserRepository repo) {
        this.repo = repo;
    }

    // Spring Security calls this with the username typed on the login page
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // look the admin up in MySQL; throw an error if there is no such username
        AdminUser admin = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        // convert our AdminUser into the object Spring Security understands
        return User.withUsername(admin.getUsername())
                .password(admin.getPassword())      // the BCrypt hash from MySQL; Spring compares it for us
                .roles("ADMIN")                     // every admin gets the role ADMIN
                .build();
    }
}