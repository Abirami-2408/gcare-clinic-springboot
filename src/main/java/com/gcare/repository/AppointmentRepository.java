package com.gcare.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gcare.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // true if the slot is already used by a booking that is not cancelled
    boolean existsByAppointmentDateAndAppointmentTimeAndStatusNot(
            LocalDate date, String time, String status);

    // newest dates first for the admin list
    List<Appointment> findAllByOrderByAppointmentDateDescCreatedAtDesc();
    // counts the bookings on a date, ignoring cancelled ones
    long countByAppointmentDateAndStatusNot(LocalDate date, String status);
    // finds a booking only when BOTH the id and the phone number match
    Optional<Appointment> findByIdAndPhone(Long id, String phone);
}