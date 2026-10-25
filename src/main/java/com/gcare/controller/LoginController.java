package com.gcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // shows the login page; Spring Security itself handles the form submission
    @GetMapping("/login")
    public String login() {
        return "login";                  // templates/login.html
    }
}