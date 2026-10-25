package com.gcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gcare.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStatusTrue();                // active reviews only
}