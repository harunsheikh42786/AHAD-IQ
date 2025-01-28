package com.ahad.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ahad.entities.Quiz;
import com.ahad.entities.User;
import com.ahad.helper.Message;
import com.ahad.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

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
        model.addAttribute("message", new Message());
    }

    @GetMapping("/app")
    public String profilePage() {
        return "app";
    }

    @GetMapping("/quiz-form")
    public String quizzForm() {
        return "user/quiz-form";
    }

    @GetMapping("/quizzes")
    public String quizzesPage(Model model, Principal principal) {
        try {
            User user = this.userService.getUserByEmail(principal.getName());
            model.addAttribute("quizzes", user.getQuizzes());
        } catch (Exception e) {
            model.addAttribute("quizzes", null);
            System.out.println(e.getMessage());
        }
        return "user/quizzes";
    }
}
