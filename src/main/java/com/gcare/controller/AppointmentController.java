package com.gcare.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcare.entity.Appointment;
import com.gcare.service.AppointmentService;

import jakarta.validation.Valid;

@Controller
public class AppointmentController {

    // the time slots patients can choose (clinic hours: 9 AM to 7 PM)
    private static final List<String> SLOTS = List.of(
            "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM",
            "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM");

    // the most bookings accepted on one date
    private static final int MAX_PER_DAY = 10;

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @ModelAttribute("slots")
    public List<String> slots() { return SLOTS; }

    @ModelAttribute("today")
    public String today() { return LocalDate.now().toString(); }

    // stop the form from setting these fields, even if someone edits the request by hand
    @InitBinder("appointment")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "status", "createdAt");
    }

    // ---------- booking ----------

    @GetMapping("/appointment")
    public String form(Model model) {
        model.addAttribute("appointment", new Appointment());
        return "appointment";
    }

    @PostMapping("/appointment")
    public String submit(@Valid @ModelAttribute("appointment") Appointment appointment,
                         BindingResult result) {

        LocalDate date = appointment.getAppointmentDate();
        String time = appointment.getAppointmentTime();

        // rule 1: the clinic is closed on Sundays
        if (date != null && date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            result.rejectValue("appointmentDate", "closed", "The clinic is closed on Sundays");
        }

        // rule 2: the date must not be full
        if (date != null && !result.hasFieldErrors("appointmentDate")
                && service.isDayFull(date, MAX_PER_DAY)) {
            result.rejectValue("appointmentDate", "full",
                    "This date is fully booked. Please choose another date.");
        }

        // rule 3: the time must be one of our slots, and the slot must be free
        if (time != null && !time.isBlank()) {
            if (!SLOTS.contains(time)) {
                result.rejectValue("appointmentTime", "invalid", "Please choose a valid time slot");
            } else if (date != null && !result.hasFieldErrors("appointmentDate")
                    && service.isSlotTaken(date, time)) {
                result.rejectValue("appointmentTime", "taken",
                        "This slot is already booked. Please choose another time.");
            }
        }

        // if any rule failed, show the form again with the messages
        if (result.hasErrors()) {
            return "appointment";
        }

        // all rules passed: save, then go to the success page
        Appointment saved = service.book(appointment);
        return "redirect:/appointment/success?ref=" + saved.getId();
    }

    @GetMapping("/appointment/success")
    public String success(@RequestParam(required = false) Long ref, Model model) {
        model.addAttribute("ref", ref);
        return "appointment-success";
    }

    // ---------- tracking ----------

    @GetMapping("/appointment/track")
    public String trackForm(@RequestParam(required = false) String ref, Model model) {
        model.addAttribute("ref", ref);
        return "appointment-track";
    }

    @PostMapping("/appointment/track")
    public String track(@RequestParam String ref, @RequestParam String phone, Model model) {
        model.addAttribute("ref", ref);

        Long id = parseRef(ref);                              // "GC-1" or "1" becomes 1
        Optional<Appointment> found = (id == null)
                ? Optional.empty()
                : service.track(id, phone.trim());

        if (found.isPresent()) {
            model.addAttribute("appointment", found.get());
        } else {
            model.addAttribute("notFound", true);
        }
        return "appointment-track";
    }

    // turns "GC-12", "gc-12" or "12" into the number 12; returns null if it is not a number
    private Long parseRef(String ref) {
        try {
            return Long.parseLong(ref.trim().toUpperCase().replace("GC-", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}