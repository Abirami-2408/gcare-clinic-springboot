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

import com.gcare.entity.Slider;
import com.gcare.service.SliderService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/sliders")
public class AdminSliderController {

    private final SliderService service;

    public AdminSliderController(SliderService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("sliders", service.getAll());
        return "admin/sliders";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Slider s = new Slider();
        s.setStatus(true);                             // new sliders start active
        model.addAttribute("slider", s);
        return "admin/slider-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("slider", service.getById(id));
        return "admin/slider-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Slider slider,
                       BindingResult result,           // must come right after @Valid
                       @RequestParam("imageFile") MultipartFile imageFile,
                       Model model) {
        if (result.hasErrors()) {
            return "admin/slider-form";
        }
        try {
            service.save(slider, imageFile);
        } catch (IllegalArgumentException | IOException e) {
            model.addAttribute("uploadError", e.getMessage());
            return "admin/slider-form";
        }
        return "redirect:/admin/sliders";
    }
    @PostMapping("/toggle/{id}")                 // POST: changing data must not be a plain link
    public String toggle(@PathVariable Long id) {
        service.toggleStatus(id);
        return "redirect:/admin/sliders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);                      // also removes the image file from uploads
        return "redirect:/admin/sliders";
    }
}