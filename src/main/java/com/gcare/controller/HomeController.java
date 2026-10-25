package com.gcare.controller;                       // must match the folder

import org.springframework.stereotype.Controller;   // marks this class as a web controller
import org.springframework.ui.Model;                // carries data from Java to the HTML page
import org.springframework.web.bind.annotation.GetMapping;

import com.gcare.service.ReviewService;
import com.gcare.service.ServiceItemService;
import com.gcare.service.SliderService;

@Controller
public class HomeController {

    // three helpers that fetch data from MySQL
    private final SliderService sliderService;
    private final ServiceItemService serviceItemService;
    private final ReviewService reviewService;

    // Spring fills these three automatically (constructor injection)
    public HomeController(SliderService sliderService,
                          ServiceItemService serviceItemService,
                          ReviewService reviewService) {
        this.sliderService = sliderService;
        this.serviceItemService = serviceItemService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")                                 // runs when someone opens the home URL
    public String home(Model model) {
        model.addAttribute("sliders", sliderService.getActive());          // only status = true
        model.addAttribute("services", serviceItemService.getActive());
        model.addAttribute("reviews", reviewService.getActive());
        return "index";                              // loads templates/index.html
    }
}