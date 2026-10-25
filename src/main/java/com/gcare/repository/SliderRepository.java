package com.gcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gcare.entity.Slider;

public interface SliderRepository extends JpaRepository<Slider, Long> {
    List<Slider> findByStatusTrue();      // active sliders for the home page
}