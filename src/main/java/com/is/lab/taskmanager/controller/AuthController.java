package com.is.lab.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Returnează numele fișierului login.html
    }
}
