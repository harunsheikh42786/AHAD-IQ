package com.ahad.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ahad.entities.User;
import com.ahad.helper.Message;
import com.ahad.services.UserService;

@Controller
public class HomeCotroller {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void modelHandler(Model model, Principal principal) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            // User is authenticated
            model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        } else {
            // User is not authenticated
            model.addAttribute("user", new User());
        }
        model.addAttribute("message", new Message("", ""));
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/registration")
    public String registrationPage(@ModelAttribute User user, Model model,
            Principal principal) {
        if (principal != null) {
            model.addAttribute("message",
                    new Message("You are already logged in. Logout to register a new account.", "warning"));
            return "login";
        }

        try {
            User existingUser = this.userService.getUserByEmail(user.getEmail());
            if (existingUser != null) {
                model.addAttribute("message",
                        new Message("User already exists with this email. Please log in instead.", "warning"));
                return "login";
            }

            User createdUser = this.userService.createUser(user);
            if (createdUser == null) {
                throw new Exception("Failed to create new User.");
            }

            // Success message
            model.addAttribute("message", new Message("User successfully registered. Please log in.", "success"));
            return "login"; // Redirect to login page
        } catch (Exception e) {
            model.addAttribute("message", new Message("Error: " + e.getMessage(), "danger"));
        }

        return "register"; // Return to registration page in case of errors
    }

}
