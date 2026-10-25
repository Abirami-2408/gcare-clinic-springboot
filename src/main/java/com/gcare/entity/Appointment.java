package com.gcare.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name is too long")
    private String patientName;                      // column: patient_name

    @Pattern(regexp = "^[0-9]{10}$", message = "Enter a 10-digit phone number")
    private String phone;

    @Email(message = "Enter a valid email address")  // an empty email is allowed
    @Size(max = 100)
    private String email;

    @NotNull(message = "Please choose a date")
    @FutureOrPresent(message = "The date cannot be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)   // reads the yyyy-MM-dd text from the date input
    private LocalDate appointmentDate;               // column: appointment_date

    @NotBlank(message = "Please choose a time slot")
    private String appointmentTime;                  // column: appointment_time, e.g. "10:00 AM"

    @Size(max = 500, message = "Message must be under 500 characters")
    private String message;

    private String status;                           // PENDING, CONFIRMED, COMPLETED or CANCELLED

    private LocalDateTime createdAt;                 // column: created_at

    @PrePersist                                      // runs automatically just before the first INSERT
    void onCreate() {
        if (status == null) status = "PENDING";
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}