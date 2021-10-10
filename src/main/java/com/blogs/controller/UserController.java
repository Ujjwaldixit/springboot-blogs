package com.blogs.controller;

import com.blogs.model.User;
import com.blogs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationForm";
    }

    @PostMapping("/saveUser")
    public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        boolean checkRegistered = userService.register(user);
        if (checkRegistered) {
            redirectAttributes.addFlashAttribute("success", "!!! Registered Successfully !!!");
        } else {
            redirectAttributes.addFlashAttribute("error", "!!! Already Registered !!!");
        }
        return "redirect:/register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "loginForm";
    }
}
