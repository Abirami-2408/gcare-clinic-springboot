package com.gcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gcare.entity.ServiceItem;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    List<ServiceItem> findByStatusTrue();  // active services for the home page
}