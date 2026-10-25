package com.gcare.controller.admin;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gcare.entity.ServiceItem;
import com.gcare.service.ServiceItemService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/services")
public class AdminServiceController {

    private final ServiceItemService service;

    public AdminServiceController(ServiceItemService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("services", service.getAll());
        return "admin/services";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        ServiceItem s = new ServiceItem();
        s.setStatus(true);                          // new services start active
        model.addAttribute("serviceItem", s);
        return "admin/service-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("serviceItem", service.getById(id));
        return "admin/service-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("serviceItem") ServiceItem serviceItem,
                       BindingResult result,        // must come right after @Valid
                       @RequestParam("imageFile") MultipartFile imageFile,
                       Model model) {
        if (result.hasErrors()) {
            return "admin/service-form";
        }
        try {
            service.save(serviceItem, imageFile);
        } catch (IllegalArgumentException | IOException e) {
            model.addAttribute("uploadError", e.getMessage());
            return "admin/service-form";
        }
        return "redirect:/admin/services";
    }

    @PostMapping("/toggle/{id}")                 // POST: changing data must not be a plain link
    public String toggle(@PathVariable Long id) {
        service.toggleStatus(id);
        return "redirect:/admin/services";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);                      // also removes the image file from uploads
        return "redirect:/admin/services";
    }
}