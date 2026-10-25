package com.gcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gcare.service.ReviewService;

@Controller
public class PublicReviewController {

    private final ReviewService service;

    public PublicReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping("/reviews")                                // URL: /reviews
    public String reviews(Model model) {
        model.addAttribute("reviews", service.getActive()); // active reviews from MySQL
        return "public-review";                            // matches templates/public-review.html
    }
}