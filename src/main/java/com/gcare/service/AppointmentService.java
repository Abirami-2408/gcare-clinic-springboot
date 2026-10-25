package com.gcare.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gcare.entity.Appointment;
import com.gcare.repository.AppointmentRepository;

@Service
public class AppointmentService {

    // the only status values allowed
    public static final List<String> STATUSES =
            List.of("PENDING", "CONFIRMED", "COMPLETED", "CANCELLED");

    private final AppointmentRepository repo;

    public AppointmentService(AppointmentRepository repo) {
        this.repo = repo;
    }

    // a slot is taken if a booking exists for it and was not cancelled
    public boolean isSlotTaken(LocalDate date, String time) {
        return repo.existsByAppointmentDateAndAppointmentTimeAndStatusNot(date, time, "CANCELLED");
    }

    public Appointment book(Appointment appointment) {
        appointment.setId(null);                 // always create a new row
        appointment.setStatus("PENDING");        // patients cannot choose their own status
        return repo.save(appointment);
    }

    // NEW: used by the Track page; the reference AND the phone must match
    public Optional<Appointment> track(Long id, String phone) {
        return repo.findByIdAndPhone(id, phone);
    }

    public List<Appointment> getAll() {
        return repo.findAllByOrderByAppointmentDateDescCreatedAtDesc();
    }

    public void updateStatus(Long id, String status) {
        if (!STATUSES.contains(status)) {        // reject unknown values
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        Appointment a = repo.findById(id).orElseThrow();
        a.setStatus(status);
        repo.save(a);
    }
    // true when the date already has "max" active bookings
    public boolean isDayFull(LocalDate date, int max) {
        return repo.countByAppointmentDateAndStatusNot(date, "CANCELLED") >= max;
    }
    public void delete(Long id) {
        repo.deleteById(id);
    }
}