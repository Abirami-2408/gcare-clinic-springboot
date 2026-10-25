package com.gcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gcare.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    // used at login: SELECT * FROM admin_users WHERE username = ?
    Optional<AdminUser> findByUsername(String username);
}