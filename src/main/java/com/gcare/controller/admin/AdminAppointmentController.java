package com.gcare.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcare.service.AppointmentService;

@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService service;

    public AdminAppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("appointments", service.getAll());
        model.addAttribute("statuses", AppointmentService.STATUSES);
        return "admin/appointments";
    }

    @PostMapping("/status/{id}")                     // POST: changing data should not be a plain link
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        service.updateStatus(id, status);
        return "redirect:/admin/appointments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/appointments";
    }

}