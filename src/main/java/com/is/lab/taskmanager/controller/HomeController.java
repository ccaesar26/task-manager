package com.is.lab.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * A simple controller to handle requests to the root URL.
 * Its purpose is to redirect users to the main application page.
 */
@Controller
public class HomeController {

    /**
     * Handles requests to the root ("/") path.
     *
     * @return a redirect instruction to the browser, pointing it to the projects list page.
     */
    @GetMapping("/")
    public String home() {
        // This tells the browser to make a new request to "/projects"
        return "redirect:/projects";
    }
}