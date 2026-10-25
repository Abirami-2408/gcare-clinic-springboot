package com.gcare.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcare.entity.Review;
import com.gcare.service.ReviewService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final ReviewService service;

    public AdminReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reviews", service.getAll());
        return "admin/reviews";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Review r = new Review();
        r.setStatus(true);                             // checkbox starts ticked
        model.addAttribute("review", r);
        return "admin/review-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("review", service.getById(id));
        return "admin/review-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Review review, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/review-form";                // show the form again with messages
        }
        service.save(review);
        return "redirect:/admin/reviews";
    }

    @PostMapping("/toggle/{id}")                 // POST: changing data must not be a plain link
    public String toggle(@PathVariable Long id) {
        service.toggleStatus(id);
        return "redirect:/admin/reviews";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/reviews";
    }
}